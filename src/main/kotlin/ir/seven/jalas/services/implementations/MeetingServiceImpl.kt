package ir.seven.jalas.services.implementations

import ir.seven.jalas.dto.*
import ir.seven.jalas.clients.reservation.ReservationClient
import ir.seven.jalas.entities.*
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.enums.MeetingParticipationRole
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.enums.UserChoiceState
import ir.seven.jalas.exceptions.BadRequestException
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.exceptions.InternalServerError
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.services.*
import ir.seven.jalas.utilities.toDate
import ir.seven.jalas.utilities.toSimpleDateFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.util.*
import kotlin.math.absoluteValue

@Service
@Transactional
class MeetingServiceImpl : MeetingService {

    @Autowired
    private lateinit var meetingRepo: MeetingRepo

    @Autowired
    private lateinit var slotService: SlotService

    @Autowired
    private lateinit var reservationClient: ReservationClient

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var emailService: EmailService

    @Autowired
    private lateinit var participationService: ParticipationService

    val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    override fun createMeeting(username: String, request: CreateMeetingRequest): MeetingInfo {
        val meeting = Meeting(title = request.title)

        meeting.slots = request.slots.map {
            Slot(meeting = meeting, startDate = it.from.toDate(), endDate = it.to.toDate())
        }.toMutableList()

        meeting.participants.add(
                Participants(
                        user = userService.getUserObjectByUsername(username),
                        meeting = meeting,
                        role = MeetingParticipationRole.CREATOR
                )
        )

        val meetingObject = meetingRepo.save(meeting)

        participationService.addParticipantsToMeeting(meeting, request.participants)

        logger.info("Create meeting ${meeting.mid}")

        return MeetingInfo(meetingObject)
    }

    override fun addSlot(meetingId: String, request: CreateSlotRequest): MeetingInfo {
        val meeting = getMeetingObjectById(meetingId)

        meeting.slots.add(
                Slot(meeting = meeting, startDate =  request.from.toDate(), endDate = request.to.toDate())
        )

        meeting.participants.forEach { participants ->
            emailService.sendAddSlotEmail(meeting.title, participants.user.username)
        }

        val savedMeeting = meetingRepo.save(meeting)

        logger.info("Add new slot to meeting $meetingId")

        return MeetingInfo(savedMeeting)
    }

    override fun getMeetingById(meetingId: String): MeetingInfo {
        val meeting = getMeetingObjectById(meetingId)
        return MeetingInfo(meeting)
    }

    override fun getMeetingObjectById(meetingId: String): Meeting {
        val meeting = meetingRepo.findById(meetingId)
        if (meeting.isPresent)
            return meeting.get()
        throw EntityDoesNotExist(ErrorMessage.MEETING_DOES_NOT_EXIST)
    }

    override fun getAllMeetings(): List<MeetingInfo> {
        return meetingRepo.findAll().map { MeetingInfo(it) }
    }

    override fun chooseSlot(meetingId: String, slotId: String): MeetingInfo {
        val meeting = getMeetingObjectById(meetingId)
        val slot = slotService.getSlotObjectById(slotId)

        meeting.slotId = slot
        meeting.state = MeetingStatus.TIME_SUBMITTED

        val savedMeeting = meetingRepo.save(meeting)
        return MeetingInfo(savedMeeting)
    }

    override fun voteSlot(meetingId: String, slotId: String, username: String, vote: UserChoiceState): MeetingInfo {
        slotService.voteSlot(slotId, username, vote)

        val meeting = getMeetingObjectById(meetingId)
        val meetingCreator = meeting.getMeetingCreator()

        emailService.sendNewVoteEmail(meeting.title, username, meetingCreator.getEmail())

        return MeetingInfo(meeting)
    }

    override fun getAvailableRooms(meetingId: String): AvailableRooms {
        val meeting = getMeetingObjectById(meetingId)

        if (meeting.state >= MeetingStatus.TIME_SUBMITTED && meeting.slotId != null) {
            val slot = meeting.slotId!!

            try {
                return reservationClient.getAllAvailableRooms(
                        slot.startDate.toSimpleDateFormat(),
                        slot.endDate.toSimpleDateFormat()
                )
            } catch (exp: Exception) {
                logger.error(exp.message)
                throw InternalServerError(ErrorMessage.RESERVATION_SYSTEM_NOT_RESPONDING)
            }
        }
        throw BadRequestException(ErrorMessage.CAN_NOT_SET_ROOM_BEFORE_SETTING_TIME)
    }

    override fun chooseRoom(meetingId: String, roomId: Int): MeetingInfo {
        val meeting = getMeetingObjectById(meetingId)

        if (meeting.slotId == null)
            throw BadRequestException(ErrorMessage.CAN_NOT_SET_ROOM_BEFORE_SETTING_TIME)

        meeting.roomId = roomId
        meeting.state = MeetingStatus.ROOM_SUBMITTED

        val savedObject = meetingRepo.save(meeting)
        return MeetingInfo(savedObject)
    }

    override fun changeMeetingState(meetingId: String, status: MeetingStatus) : MeetingInfo {
        val meeting = getMeetingObjectById(meetingId)

        when (meeting.state) {
            MeetingStatus.CANCELLED, MeetingStatus.RESERVED ->
                throw BadRequestException(ErrorMessage.THIS_MEETING_IS_CANCELLED)

            MeetingStatus.ROOM_SUBMITTED, MeetingStatus.TIME_SUBMITTED ->
                if (status == MeetingStatus.PENDING)
                    meeting.changed = true

            MeetingStatus.POLL ->
                if (status == MeetingStatus.PENDING) {
                    participationService.notifyMeetingIsClosed(meetingId)
                }
        }

        if (status == MeetingStatus.RESERVED)
            meeting.submitTime = Date()

        meeting.state = status

        return MeetingInfo(meetingRepo.save(meeting))
    }

    override fun getTotalReservedRoomsCount(): Int {
        val meetings = meetingRepo.findAll()
        return meetings.filter { it.state == MeetingStatus.RESERVED }.size
    }

    override fun getTotalCancelledMeetings(): Int {
        val meetings = meetingRepo.findAll()
        return meetings.filter { it.state == MeetingStatus.CANCELLED }.size
    }

    override fun getTotalChangedMeetings(): Int {
        val meetings = meetingRepo.findAll()
        return meetings.filter { it.changed }.size
    }

    override fun getAverageMeetingCreationTime(): Double {
        val meetings = meetingRepo.findAll()
        return meetings.filter { it.state == MeetingStatus.RESERVED }.map { meeting ->
            meeting.getMeetingCreationTime()
        }.average().absoluteValue
    }

    override fun isMeetingCreator(username: String, meetingId: String): Boolean {
        val creator = getMeetingObjectById(meetingId).getMeetingCreator()

        if (creator.username == username)
            return true
        return false
    }

    override fun hasParticipatedInMeeting(username: String, meetingId: String): Boolean {
        return getMeetingObjectById(meetingId).isParticipated(username)
    }
}
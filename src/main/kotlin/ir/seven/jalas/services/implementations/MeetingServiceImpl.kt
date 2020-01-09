package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.*
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
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.SlotService
import ir.seven.jalas.services.UserService
import net.bytebuddy.utility.RandomString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Instant
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

    val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    override fun createMeeting(username: String, request: CreateMeetingRequest): MeetingInfo {
        val user = userService.getUserObjectByUsername(username)

        val meeting = Meeting(
                mid = RandomString.make(10),
                title = request.title
        )

        meeting.participants.add(
                Participants(
                        user = user,
                        meeting = meeting,
                        role = MeetingParticipationRole.CREATOR
                )
        )

        meeting.slots = request.slots.map {
            Slot(
                    meeting = meeting,
                    startDate = Date.from(Instant.ofEpochSecond(it.from)),
                    endDate = Date.from(Instant.ofEpochSecond(it.to))
            )
        }.toMutableList()

        meeting.participants = request.participants.map { participantUsername ->
            Participants(
                    user = userService.getUserObjectByUsername(participantUsername),
                    meeting = meeting
            )
        }.toMutableList()

        val meetingObject = meetingRepo.save(meeting)

        logger.info("Create meeting ${meeting.mid}")

        request.participants.forEach {
            emailService.sendMeetingInvitationEmail(meetingObject, it)
        }

        return MeetingInfo(meetingObject)
    }

    override fun addSlot(meetingId: String, request: CreateSlotRequest): MeetingInfo {
        val meeting = getMeetingObjectById(meetingId)

        meeting.slots.add(
                Slot(
                        meeting = meeting,
                        startDate =  Date.from(Instant.ofEpochSecond(request.from)),
                        endDate = Date.from(Instant.ofEpochSecond(request.to))
                )
        )

        meeting.participants.forEach { participants ->
            emailService.sendAddSlotEmail(meeting.title, participants.user.username)
        }

        val savedMeeting = meetingRepo.save(meeting)

        logger.info("Add new slot $ to meeting $meetingId")

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
        val meeting = getMeetingObjectById(meetingId)
        val slot = slotService.getSlotObjectById(slotId)
        val user = userService.getUserObjectByUsername(username)

        val meetingSlot = meeting.slots.find { it.slotId == slotId } ?:
                throw EntityDoesNotExist(ErrorMessage.SLOT_DOES_NOT_EXIST)
        val slotChoice = meetingSlot.usersChoices.find {
            it.slot.slotId == slotId && it.user.userId == user.userId
        }

        if (slotChoice == null)
            meetingSlot.usersChoices.add(
                UserChoice(
                        id = RandomString.make(6),
                        user = user,
                        slot = slot,
                        state = vote
                )
        ) else {
            slotChoice.state = vote
        }

        val savedObject = meetingRepo.save(meeting)

        val meetingCreator = meeting.getMeetingCreator()

        emailService.sendNewVoteEmail(meeting.title, username, meetingCreator.getEmail())

        logger.info("User: $username vote for meeting: $meetingId")

        return MeetingInfo(savedObject)
    }

    override fun getAvailableRooms(meetingId: String): AvailableRooms {
        val meeting = getMeetingObjectById(meetingId)
        if (meeting.state >= MeetingStatus.TIME_SUBMITTED && meeting.slotId != null) {
            val slot = meeting.slotId!!

            val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val from = SimpleDateFormat(dateFormat).format(slot.startDate)
            val to = SimpleDateFormat(dateFormat).format(slot.endDate)
            try {
                return reservationClient.getAllAvailableRooms(from, to)
            } catch (exp: Exception) {
                logger.error(exp.message)
                throw InternalServerError("Reservation system error not responding")
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

        if (meeting.state == MeetingStatus.CANCELLED || meeting.state == MeetingStatus.RESERVED)
            throw BadRequestException(ErrorMessage.THIS_MEETING_IS_CANCELLED)

        if ((meeting.state == MeetingStatus.ROOM_SUBMITTED ||
                        meeting.state == MeetingStatus.TIME_SUBMITTED) &&
                status == MeetingStatus.PENDING)
            meeting.changed = true

        if (status == MeetingStatus.RESERVED)
            meeting.submitTime = Date()

        meeting.state = status
        val savedObject = meetingRepo.save(meeting)

        return MeetingInfo(savedObject)
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
            (meeting.submitTime?.time ?: 0L) - meeting.creationTime.time
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
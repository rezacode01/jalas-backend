package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.CreateMeetingRequest
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.DTO.VoteMeetingRequest
import ir.seven.jalas.clients.reservation.ReservationClient
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.Slot
import ir.seven.jalas.entities.UserChoice
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.enums.UserChoiceState
import ir.seven.jalas.exceptions.BadRequestException
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.exceptions.InternalServerError
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.repositories.UserRepo
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.SlotService
import ir.seven.jalas.services.UserService
import net.bytebuddy.utility.RandomString
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

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

    val logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    override fun createMeeting(username: String, request: CreateMeetingRequest): MeetingInfo {
        val user = userService.getUserObjectByUsername(username)

        val meeting = Meeting(
                mid = RandomString.make(10),
                title = request.title,
                creator = user
        )

        val slots = request.slots.map {
            Slot(
                    slotId = RandomString.make(10),
                    meeting = meeting,
                    startDate = Date.from(Instant.ofEpochSecond(it.from)),
                    endDate = Date.from(Instant.ofEpochSecond(it.to))
            )
        }.toMutableList()

        meeting.slots = slots

        val meetingObject = meetingRepo.save(meeting)

        logger.info("Create meeting ${meeting.mid}")

        request.participants.forEach {
            emailService.sendMeetingInvitationEmail(meetingObject, it)
        }

        return MeetingInfo(meetingObject)
    }

    override fun getMeetingById(meetingId: String): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        return MeetingInfo(meeting)
    }

    override fun getMeetingObjectById(meetingId: String): Meeting {
        return getMeetingByIdAndHandleException(meetingId)
    }

    override fun getAllMeetings(): List<MeetingInfo> {
        return meetingRepo.findAll().map { MeetingInfo(it) }
    }

    override fun chooseSlot(meetingId: String, slotId: String): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        val slot = slotService.getSlotObjectById(slotId)

        meeting.slotId = slot
        meeting.state = MeetingStatus.TIME_SUBMITTED

        val savedMeeting = meetingRepo.save(meeting)
        return MeetingInfo(savedMeeting)
    }

    override fun voteSlot(meetingId: String, slotId: String, request: VoteMeetingRequest): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        val slot = slotService.getSlotObjectById(slotId)
        val user = userService.getOrCreateUser(request.username)

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
                        state = request.vote
                )
        ) else {
            slotChoice.state = request.vote
        }

        val savedObject = meetingRepo.save(meeting)
        return MeetingInfo(savedObject)
    }

    override fun getAvailableRooms(meetingId: String): AvailableRooms {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        if (meeting.state >= MeetingStatus.TIME_SUBMITTED && meeting.slotId != null) {
            val slot = meeting.slotId!!

            val dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"
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
        val meeting = getMeetingByIdAndHandleException(meetingId)

        if (meeting.slotId == null)
            throw BadRequestException(ErrorMessage.CAN_NOT_SET_ROOM_BEFORE_SETTING_TIME)

        meeting.roomId = roomId
        meeting.state = MeetingStatus.ROOM_SUBMITTED

        val savedObject = meetingRepo.save(meeting)
        return MeetingInfo(savedObject)
    }

    override fun changeMeetingStats(meetingId: String, status: MeetingStatus) : MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)

        if (meeting.state == MeetingStatus.CANCELLED || meeting.state == MeetingStatus.RESERVED)
            throw BadRequestException(ErrorMessage.THIS_MEETING_IS_CANCELLED)

        meeting.state = status
        val savedObject = meetingRepo.save(meeting)

        return MeetingInfo(savedObject)
    }

    override fun getTotalReservedRoomsCount(): Int {
        val meetings = meetingRepo.findAll()
        return meetings.filter { it.state == MeetingStatus.RESERVED }.size
    }

    private fun getMeetingByIdAndHandleException(meetingId: String): Meeting {
        val meeting = meetingRepo.findById(meetingId)
        if (meeting.isPresent)
            return meeting.get()
        throw EntityDoesNotExist(ErrorMessage.MEETING_DOES_NOT_EXIST)
    }

}
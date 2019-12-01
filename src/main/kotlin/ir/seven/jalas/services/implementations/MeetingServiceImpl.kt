package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.clients.reservation.ReservationClient
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.exceptions.BadRequestException
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.exceptions.InternalServerError
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.SlotService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import java.text.SimpleDateFormat

@Service
class MeetingServiceImpl : MeetingService {

    @Autowired
    private lateinit var meetingRepo: MeetingRepo

    @Autowired
    private lateinit var slotService: SlotService

    @Autowired
    private lateinit var reservationClient: ReservationClient

    val logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    override fun getMeetingById(meetingId: String): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        return MeetingInfo(meeting)
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

    override fun changeMeetingStats(meetingId: String, status: MeetingStatus) : MeetingInfo{
        val meeting = getMeetingByIdAndHandleException(meetingId)

        if (meeting.state == MeetingStatus.CANCELLED)
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
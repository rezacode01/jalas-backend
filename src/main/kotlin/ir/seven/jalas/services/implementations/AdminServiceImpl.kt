package ir.seven.jalas.services.implementations

import ir.seven.jalas.services.AdminService
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl : AdminService {

    companion object {
        const val RESERVED_ROOMS = "Reserved rooms"
        const val CANCELLED_MEETINGS = "Cancelled meetings"
        const val CHANGED_MEETINGS = "Changed meetings"
        const val AVERAGE_RESPONSE_TIME = "Average response time"
    }

    @Autowired
    private lateinit var meetingService: MeetingService

    override fun getSystemGeneralStats(): Map<String, String> {
        val response = mutableMapOf<String, String>()

        val reservedRooms = meetingService.getTotalReservedRoomsCount()
        response[RESERVED_ROOMS] = reservedRooms.toString()

        val cancelledMeetings = meetingService.getTotalCancelledMeetings()
        response[CANCELLED_MEETINGS] = cancelledMeetings.toString()

        val changedMeetings = meetingService.getTotalChangedMeetings()
        response[CHANGED_MEETINGS] = changedMeetings.toString()

        val avgMeetingCreationTime = meetingService.getAverageMeetingCreationTime() / 1000
        response[AVERAGE_RESPONSE_TIME] = "$avgMeetingCreationTime s"

        return response
    }
}
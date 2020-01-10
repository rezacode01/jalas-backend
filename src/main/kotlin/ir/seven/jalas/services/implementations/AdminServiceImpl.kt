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

        meetingService.getAllMeetingStats().forEach { (key, value) ->
            response[key] = value.toString()
        }

        return response
    }
}
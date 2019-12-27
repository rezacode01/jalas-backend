package ir.seven.jalas.services.implementations

import ir.seven.jalas.services.AdminService
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl : AdminService {

    companion object {
        const val RESERVED_ROOMS = "Reserved rooms"
    }

    @Autowired
    private lateinit var meetingService: MeetingService

    override fun getSystemGeneralStats(): Map<String, String> {
        val response = mutableMapOf<String, String>()

        val reservedRooms = meetingService.getTotalReservedRoomsCount()
        response[RESERVED_ROOMS] = reservedRooms.toString()

        return response
    }
}
package ir.seven.jalas.controllers

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.clients.reservation.ReservationClient
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/meetings")
class MeetingController {

    @Autowired
    private lateinit var reservationClient: ReservationClient

    @Autowired
    private lateinit var meetingService: MeetingService

    @GetMapping("/available_rooms")
    fun getAvailableRooms(
            @RequestParam(required = true) start: String,
            @RequestParam(required = true) end: String
    ): AvailableRooms {
        val dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"
        val startDate: Date = SimpleDateFormat(dateFormat).parse(start)
        val endDate: Date = SimpleDateFormat(dateFormat).parse(end)

        if (startDate.after(endDate))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        return reservationClient.getAllAvailableRooms(start, end)
    }

    @GetMapping("/{meetingId}")
    fun getMeetingById(
            @PathVariable meetingId: String
    ): MeetingInfo {
        return meetingService.getMeetingById(meetingId)
    }

    @PostMapping("/{meetingId}/slots/{slotId}")
    fun chooseSlot(
            @PathVariable meetingId: String,
            @PathVariable slotId: String
    ): MeetingInfo {
        return meetingService.chooseSlot(meetingId, slotId)
    }

    @PostMapping("/{meetingId}/rooms/{roomId}")
    fun chooseRoom(
            @PathVariable meetingId: String,
            @PathVariable roomId: Int
    ): MeetingInfo {
        return meetingService.chooseRoom(meetingId, roomId)
    }
}

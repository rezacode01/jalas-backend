package ir.seven.jalas.controllers

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.CreateMeetingRequest
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.DTO.VoteMeetingRequest
import ir.seven.jalas.clients.reservation.ReservationClient
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.enums.UserChoiceState
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
import java.lang.Exception
import java.security.Principal
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/meetings")
class MeetingController {

    @Autowired
    private lateinit var meetingService: MeetingService

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createMeeting(
            @AuthenticationPrincipal principal: Principal,
            @RequestBody request: CreateMeetingRequest
    ): MeetingInfo {
        return meetingService.createMeeting(principal.name, request)
    }

    @GetMapping("/{meetingId}/available_rooms")
    fun getAvailableRooms(
            @PathVariable meetingId: String
    ): AvailableRooms {
        return meetingService.getAvailableRooms(meetingId)
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

    @PostMapping("/{meetingId}/slots/{slotId}/vote")
    fun voteForSlot(
            @PathVariable meetingId: String,
            @PathVariable slotId: String,
            @RequestBody request: VoteMeetingRequest
    ): MeetingInfo {
        return meetingService.voteSlot(meetingId, slotId, request)
    }

    @PostMapping("/{meetingId}/rooms/{roomId}")
    fun chooseRoom(
            @PathVariable meetingId: String,
            @PathVariable roomId: Int
    ): MeetingInfo {
        return meetingService.chooseRoom(meetingId, roomId)
    }

    @PostMapping("/{meetingId}/status")
    fun changeMeetingStatus(
            @PathVariable meetingId: String,
            @RequestParam status: MeetingStatus
    ) : MeetingInfo {
        return meetingService.changeMeetingStats(meetingId, status)
    }
}

package ir.seven.jalas.controllers

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.CreateMeetingRequest
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.DTO.VoteMeetingRequest
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/meetings")
class MeetingController {

    @Autowired
    private lateinit var meetingService: MeetingService

    /**
     * Participants of meeting should be exist in current users
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createMeeting(
            @AuthenticationPrincipal principal: Principal,
            @RequestBody request: CreateMeetingRequest
    ): MeetingInfo {
        return meetingService.createMeeting(principal.name, request)
    }

    /**
     * Just participants of a meeting is authorized
     * A slot should be selected for meeting
     */
    @GetMapping("/{meetingId}/available_rooms")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun getAvailableRooms(
            @AuthenticationPrincipal principal: Principal,
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

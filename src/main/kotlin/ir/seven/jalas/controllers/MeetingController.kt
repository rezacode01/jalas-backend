package ir.seven.jalas.controllers

import ir.seven.jalas.DTO.*
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.services.CommentService
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.UserService
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

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var commentService: CommentService

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

    @PostMapping("/{meetingId}/slots")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isMeetingCreator(#principal.name, #meetingId)")
    fun addSlot(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @RequestBody request: CreateSlotRequest
    ): MeetingInfo {
        return meetingService.addSlot(meetingId, request)
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

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getAllMyMeetings(
            @AuthenticationPrincipal principal: Principal
    ): NormalizedListFormat<MeetingInfo> {
        return userService.getAllMyMeeting(principal.name).toNormalizedForm()
    }

    /**
     * Just participants of a meeting could see it
     */
    @GetMapping("/{meetingId}")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun getMeetingById(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String
    ): MeetingInfo {
        return meetingService.getMeetingById(meetingId)
    }

    /**
     * Just creator of meeting could choose slot for meeting
     */
    @PostMapping("/{meetingId}/slots/{slotId}")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isMeetingCreator(#principal.name, #meetingId)")
    fun chooseSlot(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable slotId: String
    ): MeetingInfo {
        return meetingService.chooseSlot(meetingId, slotId)
    }

    /**
     * Just participants of meeting can vote
     */
    @PostMapping("/{meetingId}/slots/{slotId}/vote")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun voteForSlot(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable slotId: String,
            @RequestBody request: VoteMeetingRequest
    ): MeetingInfo {
        return meetingService.voteSlot(meetingId, slotId, principal.name, request.vote)
    }

    /**
     * Just creator of a meeting could choose room for meeting
     */
    @PostMapping("/{meetingId}/rooms/{roomId}")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isMeetingCreator(#principal.name, #meetingId)")
    fun chooseRoom(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable roomId: Int
    ): MeetingInfo {
        return meetingService.chooseRoom(meetingId, roomId)
    }

    /**
     * Just creator of a meeting could cancel or change status of meeting
     */
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isMeetingCreator(#principal.name, #meetingId)")
    @PostMapping("/{meetingId}/status")
    fun changeMeetingStatus(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @RequestParam status: MeetingStatus
    ) : MeetingInfo {
        return meetingService.changeMeetingState(meetingId, status)
    }

    @PostMapping("/{meetingId}/comments")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun commentOnMeeting(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @RequestBody request: MeetingCommentRequest
    ): CommentInfo {
        return commentService.createComment(meetingId, principal.name, request)
    }

    @GetMapping("{meetingId}/comments")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun getAllComments(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String
    ): NormalizedListFormat<CommentInfo> {
        return commentService.getComments(meetingId).toNormalizedForm()
    }

    @DeleteMapping("{meetingId}/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun deleteCommentOnMeeting(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable commentId: String
    ) {
        commentService.deleteComment(meetingId, commentId)
    }
}

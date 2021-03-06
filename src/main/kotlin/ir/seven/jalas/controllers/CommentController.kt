package ir.seven.jalas.controllers

import ir.seven.jalas.dto.CommentInfo
import ir.seven.jalas.dto.MeetingCommentRequest
import ir.seven.jalas.dto.NormalizedListFormat
import ir.seven.jalas.dto.toNormalizedForm
import ir.seven.jalas.services.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/meetings/{meetingId}/comments")
class CommentController {

    @Autowired
    private lateinit var commentService: CommentService

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun commentOnMeeting(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @RequestBody request: MeetingCommentRequest
    ): CommentInfo {
        return commentService.createComment(meetingId, principal.name, request)
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.hasParticipatedInMeeting(#principal.name, #meetingId)")
    fun getAllComments(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String
    ): NormalizedListFormat<CommentInfo> {
        return commentService.getComments(meetingId).toNormalizedForm()
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isCommentCreator(#principal.name, #commentId)")
    fun editCommentOnMeeting(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable commentId: String,
            @RequestBody request: MeetingCommentRequest
    ) : CommentInfo{
        return commentService.editComment(meetingId, commentId, request)
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER') and (@authorizationService.isMeetingCreator(#principal.name, #meetingId) or @authorizationService.isCommentCreator(#principal.name, #commentId))")
    fun deleteCommentOnMeeting(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable commentId: String
    ) {
        commentService.deleteComment(meetingId, commentId)
    }
}
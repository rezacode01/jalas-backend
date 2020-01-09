package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.CommentInfo
import ir.seven.jalas.DTO.MeetingCommentRequest
import ir.seven.jalas.entities.Comment
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.CommentRepo
import ir.seven.jalas.services.CommentService
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class CommentServiceImpl : CommentService {

    @Autowired
    private lateinit var commentRepo: CommentRepo

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var userService: UserService

    val logger : Logger = LoggerFactory.getLogger(CommentServiceImpl::class.java)

    override fun getComments(meetingId: String): List<CommentInfo> {
        val meeting = meetingService.getMeetingObjectById(meetingId)
        val comments = commentRepo.findCommentsByMeeting(meeting)

        return comments.map { comment -> CommentInfo(comment) }
    }

    override fun deleteComment(meetingId: String, commentId: String) {
        val meeting = meetingService.getMeetingObjectById(meetingId)

        meeting.comments.find { it.commentId == commentId } ?: throw EntityDoesNotExist(ErrorMessage.COMMENT_DOES_NOT_EXIST)
        meeting.comments.removeIf { it.commentId == commentId }

//        meetingRepo.save(meeting)
    }

    override fun createComment(meetingId: String, username: String, request: MeetingCommentRequest): CommentInfo {
        val meeting = meetingService.getMeetingObjectById(meetingId)
        val user = userService.getUserObjectByUsername(username)

        val repliedMeeting =
                if (request.replyTo != null)
                    meeting.comments.find { it.commentId == request.replyTo } ?:
                    throw EntityDoesNotExist(ErrorMessage.COMMENT_DOES_NOT_EXIST)
                else null

        val newComment = Comment(
                user = user,
                meeting = meeting,
                message = request.message,
                repliedComment = repliedMeeting
        )

        commentRepo.save(newComment)

        logger.info("Create comment with message: ${request.message} on meeting $meetingId")

        return CommentInfo(newComment)
    }
}
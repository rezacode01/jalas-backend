package ir.seven.jalas.services.implementations

import ir.seven.jalas.services.AuthorizationService
import ir.seven.jalas.services.CommentService
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service(value = "authorizationService")
@Transactional
class AuthorizationServiceImpl : AuthorizationService {

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var commentService: CommentService

    override fun isMeetingCreator(username: String, meetingId: String): Boolean {
        return meetingService.isMeetingCreator(username, meetingId)
    }

    override fun hasParticipatedInMeeting(username: String, meetingId: String): Boolean {
        return meetingService.hasParticipatedInMeeting(username, meetingId)
    }

    override fun isCommentCreator(username: String, commentId: String): Boolean {
        return commentService.isCommentCreator(username, commentId)
    }
}
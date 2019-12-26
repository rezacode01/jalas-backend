package ir.seven.jalas.services.implementations

import ir.seven.jalas.services.AuthorizationService
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service(value = "authorizationService")
@Transactional
class AuthorizationServiceImpl : AuthorizationService {

    @Autowired
    private lateinit var meetingService: MeetingService

    override fun hasParticipatedInMeeting(username: String, meetingId: String): Boolean {
        return meetingService.hasParticipatedInMeeting(username, meetingId)
    }
}
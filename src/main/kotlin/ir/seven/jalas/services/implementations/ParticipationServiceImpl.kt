package ir.seven.jalas.services.implementations

import ir.seven.jalas.entities.Participants
import ir.seven.jalas.repositories.ParticipationRepo
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.ParticipationService
import ir.seven.jalas.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ParticipationServiceImpl : ParticipationService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var participationRepo: ParticipationRepo

    @Autowired
    private lateinit var emailService: EmailService

    val logger: Logger = LoggerFactory.getLogger(SlotServiceImpl::class.java)

    override fun addParticipantToMeeting(username: String, meetingId: String) {
        val user = userService.getOrCreateUser(username)
        val meeting = meetingService.getMeetingObjectById(meetingId)

        participationRepo.save(Participants(user = user, meeting = meeting))

        emailService.sendNewParticipantEmail(meeting.title, user.getEmail())

        logger.info("New participant: $username added to meeting: $meetingId")
    }
}
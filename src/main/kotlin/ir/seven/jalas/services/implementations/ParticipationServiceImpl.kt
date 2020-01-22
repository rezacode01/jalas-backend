package ir.seven.jalas.services.implementations

import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.Participants
import ir.seven.jalas.entities.User
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.exceptions.BadRequestException
import ir.seven.jalas.exceptions.EntityDoesNotExist
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

    val logger: Logger = LoggerFactory.getLogger(ParticipationServiceImpl::class.java)

    override fun addParticipantsToMeeting(meeting: Meeting, participants: List<String>) {
        participants.forEach { username ->
            val user = userService.getOrCreateUser(username)

            if (! participationRepo.findByUserAndMeeting(user, meeting).isPresent) {
                participationRepo.save(Participants(user = user, meeting = meeting))

                emailService.sendMeetingInvitationEmail(meeting, username)
            }
        }
    }

    override fun addParticipantToMeeting(username: String, meetingId: String) {
        val user = userService.getOrCreateUser(username)
        val meeting = meetingService.getMeetingObjectById(meetingId)

        participationRepo.save(Participants(user = user, meeting = meeting))

        emailService.sendNewParticipantEmail(meeting.title, user.getEmail())

        logger.info("New participant: $username added to meeting: $meetingId")
    }

    override fun removeParticipantFromMeeting(username: String, meetingId: String) {
        val user = userService.getOrCreateUser(username)
        val meeting = meetingService.getMeetingObjectById(meetingId)

        if (getParticipationObjectByUser(user, meeting).isCreator())
            throw BadRequestException(ErrorMessage.USER_IS_POLL_CREATOR)

        participationRepo.deleteByUserAndMeeting(user, meeting)

        emailService.sendDeleteParticipantEmail(meeting.title, user.getEmail())

        logger.info("Delete participant: $username from meeting: $meetingId")
    }

    override fun notifyPollIsClosed(meetingId: String) {
        val meeting = meetingService.getMeetingObjectById(meetingId)

        meeting.participants.forEach { participant ->
            emailService.sendClosedPollEmail(meeting.title, participant.user.getEmail())
        }
    }

    private fun getParticipationObjectByUser(user: User, meeting: Meeting): Participants {
        val participant = participationRepo.findByUserAndMeeting(user, meeting)
        if (participant.isPresent)
            return participant.get()
        throw EntityDoesNotExist(ErrorMessage.USER_DOES_NOT_PARTICIPATED_IN_THIS_MEETING)

    }
}
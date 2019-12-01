package ir.seven.jalas.services.implementations

import ir.seven.jalas.globals.EmailBodies
import ir.seven.jalas.globals.EmailSubjects
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl : EmailService {

    private val logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    var emailSender: JavaMailSender? = null

    override fun sendMeetingReservedRoomEmail(meetingId: String) {
        val meeting = meetingService.getMeetingById(meetingId)
        val userEmail = meeting.creator.username

        sendSimpleMessage(
                userEmail,
                EmailSubjects.MEETING_RESERVED,
                EmailBodies.MEETING_RESERVED
        )

        logger.info("Send email to $userEmail for reserving room")
    }

    override fun sendSimpleMessage(to: String, subject: String, text: String) {
        val message: SimpleMailMessage = SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender?.send(message);
    }
}
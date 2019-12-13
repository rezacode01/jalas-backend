package ir.seven.jalas.services.implementations

import com.hubspot.jinjava.Jinjava
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.globals.EmailBodies
import ir.seven.jalas.globals.EmailSubjects
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl : EmailService {

    @Value("\${jalas.base-url}")
    private lateinit var baseUrl: String

    private val logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    private val engine = Jinjava()
    private val cachedTemplates = mutableMapOf<String, String>()

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

    override fun sendMeetingInvitationEmail(meeting: Meeting, email: String) {
        val arguments = mapOf<String, String>(
                "id" to meeting.mid,
                "baseUrl" to baseUrl
        )

        val template = render("meeting-invitation", arguments)

        this.mimeMessageSender(
                to = email,
                subject = "Your are invited to meeting ${meeting.title}",
                text = template
        )
    }

    private fun mimeMessageSender(to: String, subject: String, text: String) {
        val message = emailSender!!.createMimeMessage()

        val helper = MimeMessageHelper(message, true)

        helper.setSubject(subject)
        helper.setTo(to)
        helper.setText(text, text)

        emailSender?.send(message)
    }

    private fun render(name: String, map: Map<String, Any>) =
            engine.render(templateOf(name), map) ?: throw RuntimeException("Cannot render $name")

    private fun templateOf(name: String): String {
        return cachedTemplates[name] ?: {
            val loaded = javaClass.getResource("/emails/$name.html").readText()
            cachedTemplates[name] = loaded

            loaded
        }()
    }
}
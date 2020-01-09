package ir.seven.jalas.services.implementations

import com.hubspot.jinjava.Jinjava
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.globals.EmailSubjects
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
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
        val meeting = meetingService.getMeetingObjectById(meetingId)

        val receivers: MutableSet<String> = mutableSetOf()
        receivers.add(meeting.getMeetingCreator().getEmail())
        meeting.slots.forEach { slot ->
            slot.usersChoices.forEach { userChoice ->
                receivers.add(userChoice.user.username)
            }
        }

        val arguments = mapOf(
                "id" to meetingId,
                "baseUrl" to baseUrl
        )
        val template = render("meeting-link", arguments)

        receivers.forEach {
            this.mimeMessageSender(
                    it,
                    EmailSubjects.MEETING_RESERVED,
                    template
            )
        }

        logger.info("Send email for reserving room")
    }

    @Async
    override fun sendAddSlotEmail(meetingTitle: String, email: String) {
        val arguments = mapOf(
                "meeting" to meetingTitle
        )
        val template = render("meeting-add-slot", arguments)

        this.mimeMessageSender(
                to = email,
                subject = EmailSubjects.MEETING_ADD_NEW_SLOT,
                text = template
        )
    }

    @Async
    override fun sendDeleteSlotEmail(meetingTitle: String, email: String) {
        val arguments = mapOf(
                "meeting" to meetingTitle
        )
        val template = render("meeting-delete-slot", arguments)

        this.mimeMessageSender(
                to = email,
                subject = EmailSubjects.MEETING_DELETE_SLOT,
                text = template
        )
    }

    @Async
    override fun sendNewParticipantEmail(meetingTitle: String, email: String) {
        val arguments = mapOf(
                "meeting" to meetingTitle
        )
        val template = render("meeting-add-participant", arguments)

        this.mimeMessageSender(
                to = email,
                subject = EmailSubjects.MEETING_ADD_PARTICIPANT,
                text = template
        )
    }

    @Async
    override fun sendDeleteParticipantEmail(meetingTitle: String, email: String) {
        val arguments = mapOf(
                "meeting" to meetingTitle
        )
        val template = render("meeting-delete-participant", arguments)

        this.mimeMessageSender(
                to = email,
                subject = EmailSubjects.MEETING_DELETE_PARTICIPANT,
                text = template
        )
    }

    @Async
    override fun sendNewVoteEmail(meetingTitle: String, username: String, email: String) {
        val arguments = mapOf(
                "meeting" to meetingTitle,
                "user" to username
        )
        val template = render("meeting-new-vote", arguments)

        this.mimeMessageSender(
                to = email,
                subject = EmailSubjects.MEETING_NEW_VOTE,
                text = template
        )

        logger.info("Email with subject: ${EmailSubjects.MEETING_NEW_VOTE} to $email")
    }

    @Async
    override fun sendMeetingInvitationEmail(meeting: Meeting, email: String) {
        val arguments = mapOf(
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

    fun mimeMessageSender(to: String, subject: String, text: String) {
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
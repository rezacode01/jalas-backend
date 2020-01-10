package ir.seven.jalas.processes

import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.ParticipationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
@Profile("local")
class PollDeadlineChecker {

    @Autowired
    private lateinit var meetingRepo: MeetingRepo

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var participationService: ParticipationService

//    @Scheduled(fixedDelay = 5 * 60 * 1000)
    fun checkMeetingsDeadline() {
        val now = Date.from(Instant.now())
        meetingRepo.findAllByDeadlineDateNotNull().forEach { meeting ->
            val deadline = meeting.deadlineDate ?: return@forEach
            if (deadline.after(now)) {
                meetingService.changeMeetingState(meeting.mid, MeetingStatus.PENDING)

                // meetingService.setTime and slot

                participationService.notifyPollIsClosed(meeting.mid)
            }
        }
    }
}
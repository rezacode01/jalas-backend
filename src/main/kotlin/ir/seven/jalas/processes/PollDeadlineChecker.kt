package ir.seven.jalas.processes

import ir.seven.jalas.entities.Slot
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.ParticipationService
import ir.seven.jalas.services.implementations.MeetingServiceImpl
import ir.seven.jalas.utilities.toDate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
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

    val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    @Scheduled(fixedDelay = 60 * 1000)
    fun checkMeetingsDeadline() {
        logger.info("Deadline checker process start")

        val now = Date.from(Instant.now())

        meetingRepo.findAllByDeadlineDateNotNull().forEach { meeting ->
            val deadline = meeting.deadlineDate ?: return@forEach

            if (now.after(deadline) && meeting.slotId == null) {
                logger.info("Deadline checker process run for meeting: ${meeting.mid}")

                meetingService.changeMeetingState(meeting.mid, MeetingStatus.PENDING)

                meetingService.chooseSlot(meeting.mid, chooseBestSlotChoice(meeting.slots))

                meetingService.chooseRoom(meeting.mid, chooseRandomRoom(meeting.mid))

                participationService.notifyPollIsClosed(meeting.mid)

                logger.info("Deadline checker finalize meeting ${meeting.mid}")
            }
        }

        logger.info("Deadline checker process end")
    }

    fun chooseBestSlotChoice(slots: List<Slot>): String {
        var bestSlotId: String = slots.first().slotId
        val maxChoices = 0

        slots.forEach { slot ->
            if (slot.getTotalChoices() > maxChoices) {
                bestSlotId = slot.slotId
            }
        }

        return bestSlotId
    }

    fun chooseRandomRoom(meetingId: String): Int {
        val rooms = meetingService.getAvailableRooms(meetingId).availableRooms
        val randomIndex = Random().nextInt(rooms.size)
        return rooms[randomIndex]
    }
}
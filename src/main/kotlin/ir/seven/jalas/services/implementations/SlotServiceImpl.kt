package ir.seven.jalas.services.implementations

import ir.seven.jalas.dto.SlotInfo
import ir.seven.jalas.entities.Slot
import ir.seven.jalas.entities.UserChoice
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.enums.UserChoiceState
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.SlotRepo
import ir.seven.jalas.services.EmailService
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.SlotService
import ir.seven.jalas.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SlotServiceImpl : SlotService {

    @Autowired
    private lateinit var slotRepo: SlotRepo

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var emailService: EmailService

    @Autowired
    private lateinit var meetingService: MeetingService

    val logger: Logger = LoggerFactory.getLogger(SlotServiceImpl::class.java)

    override fun deleteSlot(meetingId: String, slotId: String) {
        val meeting = meetingService.getMeetingObjectById(meetingId)
        val slot = getSlotObjectById(slotId)

        val voters = slot.usersChoices.map { userChoice -> userChoice.user.username }

        voters.forEach { email ->
            emailService.sendDeleteSlotEmail(meeting.title, email)
        }

        slotRepo.deleteById(slotId)

        logger.info("Delete slot: $slotId from meeting: $meetingId")
    }

    override fun voteSlot(slotId: String, username: String, vote: UserChoiceState): SlotInfo {
        val slot = getSlotObjectById(slotId)
        val user = userService.getUserObjectByUsername(username)

        val slotChoice = slot.usersChoices.find {
            it.slot.slotId == slotId && it.user.userId == user.userId
        }

        if (slotChoice == null)
            slot.usersChoices.add(
                    UserChoice(
                            user = user,
                            slot = slot,
                            state = vote
                    )
            )
        else
            slotChoice.state = vote

        val savedObject = slotRepo.save(slot)

        logger.info("User: $username vote for slot: $slotId")

        return SlotInfo.toSlotInfo(savedObject)
    }

    override fun getSlotObjectById(slotId: String): Slot {
        val slot = slotRepo.findById(slotId)
        if (slot.isPresent)
            return slot.get()
        throw EntityDoesNotExist(ErrorMessage.SLOT_DOES_NOT_EXIST)
    }
}
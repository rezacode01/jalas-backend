package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.SlotInfo
import ir.seven.jalas.entities.Slot
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.SlotRepo
import ir.seven.jalas.services.SlotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SlotServiceImpl : SlotService {

    @Autowired
    private lateinit var slotRepo: SlotRepo

    override fun getSlotObjectById(slotId: String): Slot {
        return getSlotByIdAndHandleError(slotId)
    }

    private fun getSlotByIdAndHandleError(slotId: String): Slot {
        val slot = slotRepo.findById(slotId)
        if (slot.isPresent)
            return slot.get()
        throw EntityDoesNotExist(ErrorMessage.SLOT_DOES_NOT_EXIST)
    }
}
package ir.seven.jalas.services

import ir.seven.jalas.dto.SlotInfo
import ir.seven.jalas.entities.Slot
import ir.seven.jalas.enums.UserChoiceState


interface SlotService {
    fun deleteSlot(meetingId: String, slotId: String)
    fun voteSlot(slotId: String, username: String, vote: UserChoiceState): SlotInfo
    fun getSlotObjectById(slotId: String): Slot
}
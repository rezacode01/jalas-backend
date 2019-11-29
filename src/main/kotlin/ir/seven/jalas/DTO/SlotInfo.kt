package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Slot
import ir.seven.jalas.enums.UserChoiceState

data class SlotInfo(
     val id: String,
     val from: String,
     val to: String,
     val agreeCount: Int,
     val disAgreeCount: Int
) {
    companion object {
        fun toSlotInfo(slot: Slot, voteStats: Map<String, Int>): SlotInfo {
            return SlotInfo(
                    slot.slotId,
                    slot.startDate.toString(),
                    slot.endDate.toString(),
                    slot.usersChoices.filter { it.state == UserChoiceState.AGREE }.size,
                    slot.usersChoices.filter { it.state == UserChoiceState.DISAGREE }.size
            )
        }
    }
}
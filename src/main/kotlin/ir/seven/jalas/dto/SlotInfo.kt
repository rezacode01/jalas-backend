package ir.seven.jalas.dto

import ir.seven.jalas.entities.Slot
import ir.seven.jalas.enums.UserChoiceState
import java.text.SimpleDateFormat

data class SlotInfo(
     val id: String,
     val from: String,
     val to: String,
     val agreeCount: Int,
     val disAgreeCount: Int
) {
    companion object {
        fun toSlotInfo(slot: Slot): SlotInfo {
            return SlotInfo(
                    slot.slotId,
                    SimpleDateFormat(dateFormat).format(slot.startDate),
                    SimpleDateFormat(dateFormat).format(slot.endDate),
                    slot.usersChoices.filter { it.state == UserChoiceState.AGREE }.size,
                    slot.usersChoices.filter { it.state == UserChoiceState.DISAGREE }.size
            )
        }

        val dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
package ir.seven.jalas.dto

import ir.seven.jalas.entities.Slot
import ir.seven.jalas.enums.UserChoiceState
import ir.seven.jalas.utilities.toSimpleDateFormat
import java.text.SimpleDateFormat

data class SlotInfo(
     val id: String,
     val from: String,
     val to: String,
     val agreeCount: Int,
     val disAgreeCount: Int,
     val soSoCount: Int
) {
    companion object {
        fun toSlotInfo(slot: Slot): SlotInfo {
            return SlotInfo(
                    slot.slotId,
                    slot.startDate.toSimpleDateFormat(),
                    slot.endDate.toSimpleDateFormat(),
                    slot.getTotalAgreeCounts(),
                    slot.getTotalDisAgreeCounts(),
                    slot.getTotalSoSoCounts()
            )
        }
    }
}
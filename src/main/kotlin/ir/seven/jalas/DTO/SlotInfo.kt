package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Slot

data class SlotInfo(
     val id: String,
     val from: String,
     val to: String
) {
    constructor(slot: Slot) : this(slot.slotId, slot.startDate.toString(), slot.endDate.toString())
}
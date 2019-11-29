package ir.seven.jalas.services

import ir.seven.jalas.entities.Slot


interface SlotService {
    fun getSlotObjectById(slotId: String): Slot
}
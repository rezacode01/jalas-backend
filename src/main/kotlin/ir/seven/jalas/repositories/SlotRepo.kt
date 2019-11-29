package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Slot
import org.springframework.data.jpa.repository.JpaRepository

interface SlotRepo : JpaRepository<Slot, String>
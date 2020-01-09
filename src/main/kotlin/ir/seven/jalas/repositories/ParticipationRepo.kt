package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Participants
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipationRepo : JpaRepository<Participants, String>
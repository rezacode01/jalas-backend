package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.Participants
import ir.seven.jalas.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ParticipationRepo : JpaRepository<Participants, String> {
    fun findByUserAndMeeting(user: User, meeting: Meeting): Optional<Participants>
    fun deleteByUserAndMeeting(user: User, meeting: Meeting)
}
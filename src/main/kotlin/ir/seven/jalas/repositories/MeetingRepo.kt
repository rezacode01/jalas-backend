package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface MeetingRepo : JpaRepository<Meeting, String> {
    fun findByCreator(user: User): List<Meeting>
}
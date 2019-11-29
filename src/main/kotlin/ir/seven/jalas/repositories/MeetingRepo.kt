package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Meeting
import org.springframework.data.jpa.repository.JpaRepository

interface MeetingRepo : JpaRepository<Meeting, String>
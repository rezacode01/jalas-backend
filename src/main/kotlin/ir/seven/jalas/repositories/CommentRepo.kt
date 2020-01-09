package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Comment
import ir.seven.jalas.entities.Meeting
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepo : JpaRepository<Comment, String> {
    fun findCommentsByMeeting(meeting: Meeting): List<Comment>
}
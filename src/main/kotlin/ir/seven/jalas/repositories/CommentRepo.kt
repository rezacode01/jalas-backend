package ir.seven.jalas.repositories

import ir.seven.jalas.entities.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepo : JpaRepository<Comment, String>
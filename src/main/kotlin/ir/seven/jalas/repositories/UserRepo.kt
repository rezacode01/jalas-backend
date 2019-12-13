package ir.seven.jalas.repositories

import ir.seven.jalas.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepo : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>
}
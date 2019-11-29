package ir.seven.jalas.repositories

import ir.seven.jalas.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<User, String>
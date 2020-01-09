package ir.seven.jalas.repositories

import ir.seven.jalas.entities.UserNotificationManager
import org.springframework.data.jpa.repository.JpaRepository

interface UserNotificationManagementRepo : JpaRepository<UserNotificationManager, String>
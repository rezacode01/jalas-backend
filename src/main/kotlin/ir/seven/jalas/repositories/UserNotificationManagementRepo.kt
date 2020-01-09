package ir.seven.jalas.repositories

import ir.seven.jalas.entities.UserNotificationSetting
import org.springframework.data.jpa.repository.JpaRepository

interface UserNotificationManagementRepo : JpaRepository<UserNotificationSetting, String>
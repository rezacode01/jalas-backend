package ir.seven.jalas.services

import ir.seven.jalas.entities.UserNotificationSetting


interface UserNotificationManagementService {
    fun getOrCreateUserNotificationManagementObject(username: String): UserNotificationSetting
}
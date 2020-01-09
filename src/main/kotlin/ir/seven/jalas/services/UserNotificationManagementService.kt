package ir.seven.jalas.services

import ir.seven.jalas.entities.UserNotificationManager


interface UserNotificationManagementService {
    fun getOrCreateUserNotificationManagementObject(username: String): UserNotificationManager
}
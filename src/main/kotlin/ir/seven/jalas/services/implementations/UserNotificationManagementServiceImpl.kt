package ir.seven.jalas.services.implementations

import ir.seven.jalas.entities.UserNotificationManager
import ir.seven.jalas.repositories.UserNotificationManagementRepo
import ir.seven.jalas.services.UserNotificationManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserNotificationManagementServiceImpl : UserNotificationManagementService {

    @Autowired
    private lateinit var userNotificationManagementRepo: UserNotificationManagementRepo

    override fun getOrCreateUserNotificationManagementObject(username: String): UserNotificationManager {
        val notificationManager = userNotificationManagementRepo.findById(username)

        return if (notificationManager.isPresent)
            notificationManager.get()
        else {
            userNotificationManagementRepo.save(UserNotificationManager(id = username))
        }
    }
}
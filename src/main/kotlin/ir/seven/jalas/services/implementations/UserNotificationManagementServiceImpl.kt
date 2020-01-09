package ir.seven.jalas.services.implementations

import ir.seven.jalas.entities.UserNotificationSetting
import ir.seven.jalas.repositories.UserNotificationManagementRepo
import ir.seven.jalas.services.UserNotificationManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserNotificationManagementServiceImpl : UserNotificationManagementService {

    @Autowired
    private lateinit var userNotificationManagementRepo: UserNotificationManagementRepo

    override fun getOrCreateUserNotificationManagementObject(username: String): UserNotificationSetting {
        val notificationManager = userNotificationManagementRepo.findById(username)

        return if (notificationManager.isPresent)
            notificationManager.get()
        else {
            userNotificationManagementRepo.save(UserNotificationSetting(id = username))
        }
    }

    override fun updateUserNotificationSetting(username: String, request: UserNotificationSetting): UserNotificationSetting {
        request.id = username
        return userNotificationManagementRepo.save(request)
    }
}
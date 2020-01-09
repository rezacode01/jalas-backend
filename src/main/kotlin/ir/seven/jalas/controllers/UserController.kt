package ir.seven.jalas.controllers

import ir.seven.jalas.dto.UserInfo
import ir.seven.jalas.entities.UserNotificationSetting
import ir.seven.jalas.services.UserNotificationManagementService
import ir.seven.jalas.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userNotificationManagementService: UserNotificationManagementService

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getUserById(
            @AuthenticationPrincipal principal: Principal
    ) : UserInfo {
        return userService.getUserInfoByUsername(principal.name)
    }

    @GetMapping("/me/notification-setting")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getUserNotificationSetting(
            @AuthenticationPrincipal principal: Principal
    ): UserNotificationSetting {
        return userNotificationManagementService.getOrCreateUserNotificationManagementObject(principal.name)
    }

    @PutMapping("/me/notification-setting")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun updateUserNotificationSetting(
            @AuthenticationPrincipal principal: Principal,
            @RequestBody request: UserNotificationSetting
    ): UserNotificationSetting {
        return userNotificationManagementService.updateUserNotificationSetting(principal.name, request)
    }
}
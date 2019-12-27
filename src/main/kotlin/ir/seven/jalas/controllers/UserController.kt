package ir.seven.jalas.controllers

import ir.seven.jalas.DTO.UserInfo
import ir.seven.jalas.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
//@RequestMapping("/users ")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getUserById(
            @AuthenticationPrincipal principal: Principal
    ) : UserInfo {
        return userService.getUserInfoByUsername(principal.name)
    }
}
package ir.seven.jalas.controllers

import ir.seven.jalas.DTO.UserInfo
import ir.seven.jalas.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
//@RequestMapping("/users ")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/users/{userId}")
    fun getUserById(
            @PathVariable userId: String
    ) : UserInfo {
        return userService.getUserInfoById(userId)
    }
}
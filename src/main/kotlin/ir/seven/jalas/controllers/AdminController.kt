package ir.seven.jalas.controllers

import ir.seven.jalas.services.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController {

    @Autowired
    private lateinit var adminService: AdminService

    @GetMapping("/general")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getSystemGeneralStats(): Map<String, String> {
        return adminService.getSystemGeneralStats()
    }
}
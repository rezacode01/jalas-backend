package ir.seven.jalas.configurations.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder: BCryptPasswordEncoder()
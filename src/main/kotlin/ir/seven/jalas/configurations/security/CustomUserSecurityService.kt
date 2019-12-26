package ir.seven.jalas.configurations.security

import ir.seven.jalas.services.UserService
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
@Primary
class CustomUserSecurityService(private val userService: UserService): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null || username.isBlank())
            throw UsernameNotFoundException("Username is null or blank")

        val user = userService.getUserObjectByUsername(username)

        return object : UserDetails {
            override fun getAuthorities() = listOf(SimpleGrantedAuthority(user.role.role))
            override fun getUsername() = user.username
            override fun getPassword() = user.password
            override fun isCredentialsNonExpired() = true
            override fun isEnabled() = true
            override fun isAccountNonExpired() = true
            override fun isAccountNonLocked() = true
        }
    }
}
package ir.seven.jalas.configurations.security

import ir.seven.jalas.globals.SecurityConfigs
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
class ResourceServerConfiguration(
        private val tokenServices: DefaultTokenServices
) : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(SecurityConfigs.PANEL_API_RESOURCE_ID)
                .tokenServices(tokenServices)
    }
}
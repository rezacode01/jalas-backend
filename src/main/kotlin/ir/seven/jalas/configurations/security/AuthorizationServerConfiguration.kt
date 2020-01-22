package ir.seven.jalas.configurations.security

import ir.seven.jalas.enums.UserRole
import ir.seven.jalas.globals.SecurityConfigs
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration(
        private val authenticationManager: AuthenticationManager,
        private val customUserDetailsService: CustomUserSecurityService,
        private val passwordEncoder: PasswordEncoder,
        private val tokenStore: TokenStore,
        private val accessTokenConverter: JwtAccessTokenConverter
) : AuthorizationServerConfigurerAdapter() {
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder)
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient(SecurityConfigs.TRUSTED_CLIENT_ID)
                .authorizedGrantTypes("password", "refresh_token")
                .authorities(UserRole.TRUSTED_CLIENT.role)
                .scopes("read", "write")
                .resourceIds(SecurityConfigs.PANEL_API_RESOURCE_ID)
                .secret(passwordEncoder.encode(SecurityConfigs.TRUSTED_CLIENT_SECRET))
                .accessTokenValiditySeconds(SecurityConfigs.TRUSTED_CLIENT_ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(SecurityConfigs.TRUSTED_CLIENT_REFRESH_TOKEN_VALIDITY_SECONDS)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
    }
}
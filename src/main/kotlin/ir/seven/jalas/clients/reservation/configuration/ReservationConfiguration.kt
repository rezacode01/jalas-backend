package ir.seven.jalas.clients.reservation.configuration

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean

class ReservationConfiguration {
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor {
            it.header("Content-Type", "application/json")
        }
    }
}
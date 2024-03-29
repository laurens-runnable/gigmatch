package nl.runnable.gigmatch.app.security

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("!test")
class SecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/reference-data/skills").hasAuthority("admin")
            .and()
            .authorizeHttpRequests()
            .anyRequest().authenticated()
        http.oauth2ResourceServer().jwt()
        http.csrf().disable()
        return http.build()
    }
}

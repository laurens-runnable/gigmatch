package nl.runnable.gigmatch.security

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class KeycloakConfig {

    @Bean
    fun keycloakConfigResolver() = KeycloakSpringBootConfigResolver()

}

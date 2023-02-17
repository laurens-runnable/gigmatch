package nl.runnable.gigmatch.app.security

import nl.runnable.gigmatch.security.JwtClaimMappingConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

/**
 * Configures the mapping of JWT claims to Spring Security authentication.
 *
 * The implementation maps Keycloak-defined resource access claims to `GrantedAuthority` instances.
 * There is a maintenance/configuration burden in that the implementation needs to be aware of all the possible client IDs.
 */
@Configuration
class JwtClaimMappingConfiguration {

    @Autowired
    private lateinit var props: JwtClaimMappingConfigurationProperties

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setPrincipalClaimName(props.principalClaimName)
        converter.setJwtGrantedAuthoritiesConverter { resolveRoleAuthorities(it) }
        return converter
    }

    private fun resolveRoleAuthorities(jwt: Jwt): List<GrantedAuthority> {
        var authorities: List<GrantedAuthority> = emptyList()

        val resourceAccess = jwt.claims[props.resourceAccessClaim]
        if (resourceAccess is Map<*, *>) {
            for (clientId in props.clientIds) {
                val client = resourceAccess[clientId]
                if (client is Map<*, *>) {
                    val roles = client[props.clientRoleClaim]
                    if (roles is List<*>) {
                        authorities = roles.map { SimpleGrantedAuthority(it.toString()) }.toList()
                    }
                }
            }
        }

        return authorities
    }

}

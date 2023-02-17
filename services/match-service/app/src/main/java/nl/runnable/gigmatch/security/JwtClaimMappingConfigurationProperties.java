package nl.runnable.gigmatch.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * Defines the Spring Boot configuration properties for mapping JWT claims to Spring Security authentication.
 * <p>
 * Kotlin-based classes do not seem to work with IntelliJ autocompletion yet, hence this is a Java class.
 */
@Configuration
@ConfigurationProperties("match-service.jwt")
public class JwtClaimMappingConfigurationProperties {

    private List<String> clientIds = Collections.emptyList();

    private String principalClaimName = "preferred_username";

    private String clientRoleClaim = "roles";

    private String resourceAccessClaim = "resource_access";

    public List<String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<String> clientIds) {
        this.clientIds = clientIds;
    }

    public String getPrincipalClaimName() {
        return principalClaimName;
    }

    public void setPrincipalClaimName(String principalClaimName) {
        this.principalClaimName = principalClaimName;
    }

    public String getClientRoleClaim() {
        return clientRoleClaim;
    }

    public void setClientRoleClaim(String clientRoleClaim) {
        this.clientRoleClaim = clientRoleClaim;
    }

    public String getResourceAccessClaim() {
        return resourceAccessClaim;
    }

    public void setResourceAccessClaim(String resourceAccessClaim) {
        this.resourceAccessClaim = resourceAccessClaim;
    }
}

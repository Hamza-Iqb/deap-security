package za.co.dariel.deap.endpointsecurity.security;

import java.util.Base64;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

public class JWTUtil {
	
	public static String getJWTToken() {
        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();

        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();

        return keycloakPrincipal.getKeycloakSecurityContext().getTokenString();

    }

}

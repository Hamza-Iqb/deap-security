package za.co.dariel.deap.endpointsecurity.services;


import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KeycloakService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);
    private static final String ACTION = "Username==";

    @Value("${keycloak.credentials.secret}")
    private String secretKey;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private String admin = "admin";
    private String master = "master";

    public List<EmployeeEntity> getUserInKeyCloak(HttpServletRequest request){
        //below code uses keycloak server details to get permission to access data
        var keycloak = KeycloakBuilder.builder().serverUrl("http://localhost:8080/auth").realm(master).username(admin).password(admin)
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        //after getting permission, we now retrieve list of users from our created realm
        List<UserRepresentation> list = keycloak.realm("SpringBootKeycloak").users().list();
        list.forEach(u -> logger.info(u.getId() + ": " + u.getUsername()));

        List <EmployeeEntity> usernames = new ArrayList<>();

        for (var i=0; i< list.size(); i++){
            usernames.add(i, new EmployeeEntity(
                    list.get(i).getEmail(),
                    list.get(i).getFirstName(),
                    list.get(i).getLastName(),
                    null
                    ));
        }

        return usernames;

    }


    public String createUserInKeyCloak(EmployeeEntity employeeEntity) {
        String userId = null;
        var statusId = 0;
        try {

            UsersResource userResource = getKeycloakUserResource();

            var user = new UserRepresentation();
            user.setUsername(employeeEntity.getEmail());
            user.setEmail(employeeEntity.getEmail());
            user.setFirstName(employeeEntity.getFirstName());
            user.setLastName(employeeEntity.getLastName());
            user.setEnabled(true);

            // Create user
            Response result = userResource.create(user);
            logger.info("Keycloak create user response code>>>>" + result.getStatus());

            statusId = result.getStatus();

            if (statusId == 201) {

                userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                logger.info("User created with userId:" + userId);

                // Define password credential
                var passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(employeeEntity.getPassword());

                // Set password credential
                userResource.get(userId).resetPassword(passwordCred);


                // set role
                var realmResource = getRealmResource();
                var savedRoleRepresentation = realmResource.roles().get("app-user").toRepresentation();
                realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));

                logger.info(ACTION + employeeEntity.getUsername() + " created in keycloak successfully");

            }

            else if (statusId == 409) {
                logger.error(ACTION + employeeEntity.getUsername() + " already present in keycloak");

            } else {
                logger.error(ACTION + employeeEntity.getUsername() + " could not be created in keycloak");

            }

        } catch (Exception e) {
            logger.info("context", e);

        }

        return userId;

    }

    public void deleteUserInKeyCloak(String userId){

        UsersResource userResource = getKeycloakUserResource();
        userResource.get(userId).remove();
        logger.warn("Employee with id: " + userId + " has been removed");
    }


    // Reset password
    public void updateUserInKeyCloak(String userId, EmployeeEntity employeeEntity) {


        var user = new UserRepresentation();
        user.setEmail(employeeEntity.getEmail());
        user.setFirstName(employeeEntity.getFirstName());
        user.setLastName(employeeEntity.getLastName());

        UsersResource userResource = getKeycloakUserResource();
        userResource.get(userId).update(user);

        logger.info("User with ID: " + userId + " has been updated");
    }

    // Reset password
    public void resetPassword(String newPassword, String userId) {

        UsersResource userResource = getKeycloakUserResource();

        // Define password credential
        var passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(newPassword.trim());

        // Set password credential
        userResource.get(userId).resetPassword(passwordCred);

    }

    private UsersResource getKeycloakUserResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(authUrl).realm(master).username(admin).password(admin)
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        var realmResource = kc.realm(realm);

        return realmResource.users();
    }

    private RealmResource getRealmResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(authUrl).realm(master).username(admin).password(admin)
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();


        return kc.realm(realm);

    }


}
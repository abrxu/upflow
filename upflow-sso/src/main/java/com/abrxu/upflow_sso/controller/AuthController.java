package com.abrxu.upflow_sso.controller;

import com.abrxu.upflow_sso.dtos.LoginDTO;
import com.abrxu.upflow_sso.dtos.UserDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:8080/auth")
            .realm("master")
            .clientId("external-client")
            .clientSecret("Coi3MNPYZtho6fwSzNip9I1QvIHOEtqd")
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build();

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        UsersResource users = keycloak.realm("master").users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = users.create(userRepresentation);
        if (response.getStatus() != 201) {
            return ResponseEntity.status(response.getStatus())
                    .body("Erro ao criar usuário: " + response.getStatusInfo());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(userDTO.password());
        credentials.setTemporary(false);
        users.get(userId).resetPassword(credentials);

        return ResponseEntity.ok("Usuário criado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth")
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("external-client")
                .clientSecret("Coi3MNPYZtho6fwSzNip9I1QvIHOEtqd")
                .username(login.username())
                .password(login.password())
                .build();

        AccessTokenResponse tokenResponse = keycloak.tokenManager()
                .getAccessToken();

        // 3) Monta o mapa de retorno
        Map<String, Object> result = Map.of(
                "access_token",  tokenResponse.getToken(),
                "refresh_token", tokenResponse.getRefreshToken(),
                "expires_in",    tokenResponse.getExpiresIn()
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/ping")
    public String pong() {
        return "pong";
    }

}

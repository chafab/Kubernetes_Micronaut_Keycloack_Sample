package com.nekonex.services.employee.security;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Named("keycloak")
@Singleton
@Slf4j
//@Requires(notEnv = Environment.TEST)
//@Requires(env = {Environment.CLOUD, "k8s"})
public class KeycloakUserDetailsMapper implements OauthAuthenticationMapper {
    @Property(name = "micronaut.security.oauth2.clients.keycloak.client-id")
    private String clientId;
    @Property(name = "micronaut.security.oauth2.clients.keycloak.client-secret")
    private String clientSecret;

    @Client("${micronaut.security.oauth2.clients.keycloak.token.url}")
    @Inject
    private HttpClient client;

    KeycloakUserDetailsMapper(){
        log.info("KeycloakUserDetailsMapper() Constructor");
    }
    @Override
    public Publisher<AuthenticationResponse> createAuthenticationResponse(
            TokenResponse tokenResponse, @Nullable State state) {
        log.info("trying to retrieve user" +client.toString());
        Publisher<KeycloakUser> res = client
                .retrieve(HttpRequest.POST("/introspect",
                                "token=" + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .basicAuth(clientId, clientSecret), KeycloakUser.class);
        return Publishers.map(res, user -> {
            log.info("User: {}", user);
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("openIdToken", tokenResponse.getAccessToken());
            return AuthenticationResponse.success(user.getUsername(), user.getRoles(), attrs);
        });
    }
}
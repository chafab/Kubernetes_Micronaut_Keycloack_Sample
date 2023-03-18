package com.nekonex.services.test;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@Requires(env = Environment.TEST)
public class MockAuthenticationProvider implements AuthenticationProvider {

    public class User {
        private final String username;
        private final String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    private final List<User> users = new ArrayList<>();

    public MockAuthenticationProvider() {
        users.add(new User("john", "secret"));
        users.add(new User("Mary", "password"));
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String username = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (user.isPresent() && user.get().getPassword().equals(authenticationRequest.getSecret().toString())) {
            return Publishers.just(AuthenticationResponse.success(user.get().getUsername()));
        } else {
            return Publishers.just(AuthenticationResponse.failure("Invalid credentials"));
        }
    }
}
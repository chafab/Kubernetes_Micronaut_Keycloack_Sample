package com.nekonex.services.employee.security;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeycloakUser {
    private String email;
    private String username;
    private List<String> roles;
}

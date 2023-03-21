package com.nekonex.services.employee;

import com.nekonex.services.employee.security.KeycloakUserDetailsMapper;
import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class EmployeeApplication {

    public static void main(String[] args) {
        log.info("Start Application");
        Environment environment = Micronaut.run(EmployeeApplication.class, args).getEnvironment();
        log.info("Current environment: {}", environment.getActiveNames());
    }

}

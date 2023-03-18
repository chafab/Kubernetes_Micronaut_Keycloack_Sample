package com.nekonex.services.employee;

import com.nekonex.services.employee.model.Employee;
import io.micronaut.context.annotation.*;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;


import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.function.Executable;

@MicronautTest
@Property(name = "in-memory-store.enabled", value = "true")
@Requires(env = Environment.TEST)
public class EmployeeControllerTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerTests.class);

    @Inject
    @Client("/") // Use the custom ID here
    HttpClient client;

    @Test
    void add() throws MalformedURLException, ExecutionException, InterruptedException {
        //User didn t login
        Employee employee2 = Instancio.of(Employee.class)
                .ignore(Select.field(Employee::getId))
                .create();
        Executable e = () -> client.toBlocking().exchange(HttpRequest.POST("/api/employees", employee2).accept(TEXT_PLAIN));

        // then: 'returns unauthorized'
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());

        //User didn t login with correct credentials
        e = () -> client.toBlocking().exchange(HttpRequest.POST("/api/employees", employee2)
                .basicAuth("john", "secret2").accept(TEXT_PLAIN));

        // then: 'returns unauthorized'
        thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());


        LOGGER.info("2");

        Employee employee = Instancio.of(Employee.class)
                .ignore(Select.field(Employee::getId))
                .create();
        employee = client.toBlocking()
                .retrieve(HttpRequest.POST("/api/employees", employee)
                        .basicAuth("john", "secret"), Employee.class);
        assertNotNull(employee);
        assertNotNull(employee.getId());
    }

    @Test
    void findAll() {
        Executable e = () ->   client.toBlocking().
                retrieve(HttpRequest.GET("/api/employees"),Employee[].class);

        // then: 'returns unauthorized'
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());

        Employee[] employees = client.toBlocking().
                 retrieve(HttpRequest.GET("/api/employees")
                .basicAuth("john", "secret"),Employee[].class);
        assertTrue(employees.length > 0);
    }

    @Test
    void count() {
        //Can be accessed without login
        int val = client.toBlocking().retrieve(HttpRequest.GET("/api/employees/count"), Integer.class);
        assertEquals(val, 1);
        //Can be accessed with login
        val = client.toBlocking().retrieve(HttpRequest.GET("/api/employees/count").basicAuth("john", "secret"), Integer.class);
        assertEquals(val, 1);
        //add a user and verify count
        Employee employee = Instancio.of(Employee.class)
                .ignore(Select.field(Employee::getId))
                .create();
        employee = client.toBlocking()
                .retrieve(HttpRequest.POST("/api/employees", employee)
                        .basicAuth("john", "secret"), Employee.class);
        val = client.toBlocking().retrieve(HttpRequest.GET("/api/employees/count"), Integer.class);
        assertEquals(val, 2);
    }
}

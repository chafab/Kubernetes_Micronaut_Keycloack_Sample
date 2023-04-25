package com.nekonex.services.department;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.nekonex.services.department.model.Department;

import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentControllerTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentControllerTests.class);

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    private static Long id;

    @Test
    @Order(1)
    void add() {

        Department department2 = Instancio.of(Department.class)
                .set(Select.field(Department::getOrganizationId), 1L)
                .ignore(Select.field(Department::getId))
                .create();
        //User didn t login
        Executable e = () -> client.toBlocking().exchange(HttpRequest.POST("/api/departments", department2).accept(TEXT_PLAIN));

        // then: 'returns unauthorized'
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());

        //User didn t login with correct credentials
        e = () -> client.toBlocking().exchange(HttpRequest.POST("/api/departments", department2)
                .basicAuth("john", "secret2").accept(TEXT_PLAIN));

        // then: 'returns unauthorized'
        thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(UNAUTHORIZED, thrown.getStatus());


        LOGGER.info("2");

        Department department = Instancio.of(Department.class)
                .set(Select.field(Department::getOrganizationId), 1L)
                .ignore(Select.field(Department::getId))
                .create();
        department = client.toBlocking()
                .retrieve(HttpRequest.POST("/api/departments", department)
                        .basicAuth("john", "secret"), Department.class);
        assertNotNull(department);
        assertNotNull(department.getId());
        id = department.getId();
    }

    @Test
    @Order(2)
    void findAll() {
        Department[] departments = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/departments")
                        .basicAuth("john", "secret"), Department[].class);
        assertTrue(departments.length > 0);
    }

    @Test
    @Order(3)
    void findById() {
        Department department = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/departments/" + id)
                        .basicAuth("john", "secret"), Department.class);
        assertNotNull(department);
        assertNotNull(department.getId());
    }

    @Test
    @Order(4)
    void findByOrganization() {
        Department[] departments = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/departments/organization/" + 1L)
                        .basicAuth("john", "secret"), Department[].class);
        assertTrue(departments.length > 0);
    }
}

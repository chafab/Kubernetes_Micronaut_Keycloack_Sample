package com.nekonex.services.department.controller;

import java.util.List;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nekonex.services.department.client.EmployeeClient;
import com.nekonex.services.department.model.Department;
import com.nekonex.services.department.repository.DepartmentRepository;


@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/departments")
public class DepartmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	private DepartmentRepository repository;
	private EmployeeClient employeeClient;

	DepartmentController(DepartmentRepository repository, EmployeeClient employeeClient) {
		this.repository = repository;
		this.employeeClient = employeeClient;
	}

	@Post
	public Department add(@Body Department department) {
		LOGGER.info("Department add: {}", department);
		return repository.add(department);
	}

	@Get("/{id}")
	public Department findById(Long id) {
		LOGGER.info("Department find: id={}", id+1);
		return repository.findById(id);
		//return null; //test
	}

	@Get
	public List<Department> findAll() {
		LOGGER.info("Department find");
		return repository.findAll();
	}

	@Get("/organization/{organizationId}")
	public List<Department> findByOrganization(Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@Get("/organization/{organizationId}/with-employees")
	public List<Department> findByOrganizationWithEmployees(HttpRequest<?> request, Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		String bearerToken = request.getHeaders().getAuthorization().orElse(null);
		List<Department> departments = repository.findByOrganization(organizationId);
		departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(bearerToken, d.getId())));
		return departments;
	}

	@Get("/SecureHello")
	public String hello() {
		return "Hello authenticated user~";
	}

	@Get("/AnonymousHello")
	@Secured(SecurityRule.IS_ANONYMOUS)
	public String helloAnonymous() {
		return "Hello Anonymous";
	}
}

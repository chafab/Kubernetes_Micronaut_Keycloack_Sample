package com.nekonex.services.employee.controller;

import com.nekonex.services.employee.model.Employee;
import com.nekonex.services.employee.repository.EmployeeRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/employees")
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Inject
	EmployeeRepository repository;

	@Post
	public Employee add(@Body Employee employee) {
		LOGGER.info("Employee add: {}", employee);
		return repository.add(employee);
	}

	@Get("/{id}")
	public Employee findById(Long id) {
		LOGGER.info("Employee find: id={}", id);
		return repository.findById(id);
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

	@Get
	public List<Employee> findAll() {
		LOGGER.info("Employees find");
		return repository.findAll();
	}

	@Get("/department/{departmentId}")
	public List<Employee> findByDepartment(Long departmentId) {
		LOGGER.info("Employees find: departmentId={}", departmentId);
		return repository.findByDepartment(departmentId);
	}

	@Get("/organization/{organizationId}")
	public List<Employee> findByOrganization(Long organizationId) {
		LOGGER.info("Employees find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@Get("/count")
	long count() {
		return repository.count();
	}

}

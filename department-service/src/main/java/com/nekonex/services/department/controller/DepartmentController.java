package com.nekonex.services.department.controller;

import java.util.List;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nekonex.services.department.client.EmployeeClient;
import com.nekonex.services.department.model.Department;
import com.nekonex.services.department.repository.DepartmentRepository;


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
	public List<Department> findByOrganizationWithEmployees(Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganization(organizationId);
		departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(d.getId())));
		return departments;
	}

}

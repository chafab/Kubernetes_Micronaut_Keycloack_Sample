package com.nekonex.services.department.client;

import java.util.List;

import com.nekonex.services.department.model.Employee;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client(id = "employee", path = "/employees")
public interface EmployeeClient {

	@Get("/department/{departmentId}")
	List<Employee> findByDepartment(Long departmentId);

}

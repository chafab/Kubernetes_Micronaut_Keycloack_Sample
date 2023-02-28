package com.nekonex.services.employee.repository;

import com.nekonex.services.employee.model.Employee;

import java.util.List;

public interface EmployeeRepository {

    Employee add(Employee employee);
    Employee findById(Long id);
    List<Employee> findAll();
    List<Employee> findByDepartment(Long departmentId);
    List<Employee> findByOrganization(Long organizationId);
    long count();
}

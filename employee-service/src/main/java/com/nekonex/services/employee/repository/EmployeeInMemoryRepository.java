package com.nekonex.services.employee.repository;

import com.nekonex.services.employee.model.Employee;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.context.scope.Refreshable;
import javax.inject.Inject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//The following class is only used for tests
@Refreshable
@Requires(property = "in-memory-store.enabled", value = "true", defaultValue = "false")
public class EmployeeInMemoryRepository implements EmployeeRepository {

    @Inject
    private EmployeesInitialList employeesInitialList;

    private List<Employee> employees = new ArrayList<>();

    @Override
    public Employee add(Employee employee) {
        employee.setId((long) (employees.size() + 1));
        employees.add(employee);
        return employee;
    }

    @Override
    public Employee findById(Long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return employees;
    }

    @Override
    public List<Employee> findByDepartment(Long departmentId) {
        return employees.stream()
                .filter(employee -> employee.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findByOrganization(Long organizationId) {
        return employees.stream()
                .filter(employee -> employee.getOrganizationId().equals(organizationId))
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        employeesInitialList.getEmployees().forEach(employee -> employees.add(employee));
    }

    @Override
    public long count() {	return employees.size();	}

}
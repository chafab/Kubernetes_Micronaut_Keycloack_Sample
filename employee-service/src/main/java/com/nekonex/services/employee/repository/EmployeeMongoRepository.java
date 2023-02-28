package com.nekonex.services.employee.repository;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.nekonex.services.employee.model.Employee;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.context.scope.Refreshable;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Refreshable
@Requires(missingProperty = "in-memory-store.enabled")
public class EmployeeMongoRepository implements EmployeeRepository {

	private MongoClient mongoClient;

	@Property(name = "mongodb.database")
	private String mongodbDatabase;
	@Property(name = "mongodb.collection")
	private String mongodbCollection;

	EmployeeMongoRepository(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	@Override
	public Employee add(Employee employee) {
		employee.setId(repository().countDocuments() + 1);
		repository().insertOne(employee);
		return employee;
	}

	@Override
	public Employee findById(Long id) {
		return repository().find().first();
	}

	@Override
	public List<Employee> findAll() {
		final List<Employee> employees = new ArrayList<>();
		repository()
				.find()
				.iterator()
				.forEachRemaining(employees::add);
		return employees;
	}

	@Override
	public List<Employee> findByDepartment(Long departmentId) {
		final List<Employee> employees = new ArrayList<>();
		repository()
				.find(Filters.eq("departmentId", departmentId))
				.iterator()
				.forEachRemaining(employees::add);
		return employees;
	}

	@Override
	public List<Employee> findByOrganization(Long organizationId) {
		final List<Employee> employees = new ArrayList<>();
		repository()
				.find(Filters.eq("organizationId", organizationId))
				.iterator()
				.forEachRemaining(employees::add);
		return employees;
	}

	private MongoCollection<Employee> repository() {
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		return mongoClient.getDatabase(mongodbDatabase).withCodecRegistry(pojoCodecRegistry)
				.getCollection(mongodbCollection, Employee.class);
	}

	@Override
	public long count() {	return repository().countDocuments();	}

}

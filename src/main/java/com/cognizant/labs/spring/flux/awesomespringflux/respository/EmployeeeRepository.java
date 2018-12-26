package com.cognizant.labs.spring.flux.awesomespringflux.respository;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmployeeeRepository extends ReactiveMongoRepository<Employee, String> {

}

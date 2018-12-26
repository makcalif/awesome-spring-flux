package com.cognizant.labs.spring.flux.awesomespringflux.service;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import com.cognizant.labs.spring.flux.awesomespringflux.respository.EmployeeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {

    EmployeeeRepository employeeeRepository;

    @Autowired
    public EmployeeService(EmployeeeRepository employeeeRepository) {
        this.employeeeRepository = employeeeRepository;
    }

    public Flux<Tweet> getAll() {
        return this.employeeeRepository.findAll();
    }

    public Mono<Tweet> getById(String id) {
        return this.employeeeRepository.findById(id);
    }
}

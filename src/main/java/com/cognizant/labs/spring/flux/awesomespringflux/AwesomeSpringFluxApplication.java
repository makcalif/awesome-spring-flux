package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Employee;
import com.cognizant.labs.spring.flux.awesomespringflux.respository.EmployeeeRepository;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@SpringBootApplication
public class AwesomeSpringFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomeSpringFluxApplication.class, args);
	}

	@Bean
	CommandLineRunner populateEmployeeRepo(EmployeeeRepository employeeeRepository) {
		System.out.println("adding employees to mongo db");

		Stream.of("Jack", "Brian", "Peter", "Eric", "Maria")
				.map(name -> new Employee())
				.forEach(e -> employeeeRepository.save(e).subscribe(System.out::println));
		return args -> {
			employeeeRepository.deleteAll();
		};
	}

}


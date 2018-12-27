package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Employee;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import com.cognizant.labs.spring.flux.awesomespringflux.respository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class AwesomeSpringFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomeSpringFluxApplication.class, args);
	}

	@Bean
	CommandLineRunner populateUserRepo(UserRepository userRepository) {
		System.out.println("adding users to mongo db");

		return args -> {
			userRepository.deleteAll().subscribe(
			null,
		null,
					() -> Stream.of("Jack", "Brian", "Peter", "Eric", "Maria")
							.map(name -> new User(UUID.randomUUID().toString(), name))
							//.map(user -> userRepository.save(user))
							.forEach( u -> userRepository.save(u)
							.subscribe(System.out::println))
			);


		};
	}

}


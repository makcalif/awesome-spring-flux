package com.cognizant.labs.spring.flux.awesomespringflux;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class AwesomeSpringFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomeSpringFluxApplication.class, args);
	}

}

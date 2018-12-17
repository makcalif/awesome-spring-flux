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

@Component
class FluxConsumer implements CommandLineRunner {
	@Override
	public void run(String... args) throws Exception {
		Flux.just(1,2,3,4)
				.log()
				.subscribe(new Subscriber<Integer>() {
					@Override
					public void onSubscribe(Subscription s) {
						s.request(Long.MAX_VALUE);
					}

					@Override
					public void onNext(Integer integer) {
						System.out.println("on next called :" + integer);
					}

					@Override
					public void onError(Throwable t) {

					}

					@Override
					public void onComplete() {

					}
				});
	}
}


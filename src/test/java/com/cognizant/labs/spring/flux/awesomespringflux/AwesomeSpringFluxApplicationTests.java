package com.cognizant.labs.spring.flux.awesomespringflux;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwesomeSpringFluxApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void test1 () {

		// using sample
		// https://grokonez.com/reactive-programming/reactor/reactor-create-flux-interval-reactive-programming
		List<String> data = new ArrayList<String>(Arrays.asList("{A}", "{B}", "{C}"));
		Flux<String> intervalFlux1 = Flux
				.interval(Duration.ofMillis(500))
				.map(tick -> {
					if (tick < data.size())
						return "item " + tick + ": " + data.get(tick.intValue());
					return "Done (tick == data.size())";
				});

		intervalFlux1.take(data.size() + 1).subscribe(System.out::println);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


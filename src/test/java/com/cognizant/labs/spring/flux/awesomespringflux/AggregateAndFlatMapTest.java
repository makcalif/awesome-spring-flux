package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Employee;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Equipment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AggregateAndFlatMapTest {

    @Test
    public void sample() {
        Flux<Long> range = Flux.interval(Duration.ofSeconds(1)).take(3);

        StepVerifier.create(range)
                .expectNext(0L, 1L, 2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void zipAndPrintFlux() {
        Flux<String> first = Flux.fromIterable(Arrays.asList("A", "B", "C"));

        Flux<String> second = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> zip = Flux.zip(first, second)
        .map((f) -> f.getT1() +  f.getT2()) ;

         zip.doOnNext(System.out::println)
                 .blockLast();
    }


    @Test
    public void zipAndPrintComplexFlux() {
        Flux<Employee> employees = Flux.fromIterable(Arrays.asList(
                new Employee("123", "John", "Doe"),
                new Employee("234", "Michael", "Phelps"),
                new Employee("234", "Andre", "Aggasi")
        ));

        Flux<Equipment> equipment = Flux.fromIterable(Arrays.asList(
                new Equipment(12L, "computer", "hardware"),
                new Equipment(12L, "laptop", "hardware"),
                new Equipment(12L, "windows", "software")
        ));

        Function<Employee, Flux<String>> mapper = e -> Flux.just(e.toString());
        Flux<String> empStrFlux = employees.flatMap( mapper);

        Function<Equipment, Flux<String>> eqMapper = e -> Flux.just(e.toString());
        Flux<String> equipStrFlux = equipment.flatMap( eqMapper);

        Flux<String> merged = Flux.merge(empStrFlux, equipStrFlux);
        merged.doOnNext(System.out::println)
                .blockLast();

    }
}

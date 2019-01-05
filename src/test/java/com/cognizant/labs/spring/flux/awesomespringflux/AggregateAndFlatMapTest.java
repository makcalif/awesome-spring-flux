package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Employee;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Equipment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.IntStream;

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

        Flux<Employee> employeeBasic = Flux.fromIterable(Arrays.asList(
                new Employee("123", "John", "Doe"),
                new Employee("234", "Michael", "Phelps"),
                new Employee("234", "Andre", "Aggasi")
        ));

        Flux<Long> delay = randomDelayFlux();
        Flux<Employee> employees = Flux.zip(delay, employeeBasic)
                .map(e -> e.getT2());

        Flux<Equipment> equipmentBasic = Flux.fromIterable(Arrays.asList(
                new Equipment(12L, "computer", "hardware"),
                new Equipment(12L, "laptop", "hardware"),
                new Equipment(12L, "windows", "software")
        ));

        Flux<Equipment> equipment = Flux.zip(delay, equipmentBasic)
                .map(e -> e.getT2());

        Function<Employee, Flux<String>> mapper = e -> Flux.just(e.toString());
        Flux<String> empStrFlux = employees.flatMap( mapper);

        Function<Equipment, Flux<String>> eqMapper = e -> Flux.just(e.toString());
        Flux<String> equipStrFlux = equipment.flatMap( eqMapper);

        Flux<String> merged = Flux.merge(empStrFlux, equipStrFlux);
        merged.doOnNext(System.out::println)
                .blockLast();

    }

    private Flux<Long> randomDelayFlux() {
        ArrayList<Flux<Long>> fluxList = new ArrayList<>();
        for(int i=0; i < 5; i++) {
            fluxList.add(Flux.create((FluxSink<Long> sink) -> {
                sink.next(getRandomDelayMono().block());
                sink.complete();
            }));
        }

        Flux<Long> longFlux = Flux.create((FluxSink<Long> sink) -> {
            sink.next(getRandomDelayMono().block());
            sink.complete();
        });

        //return longFlux;
        return Flux.mergeSequential(fluxList);
    }

    @Test
    public void testRandom() {
        randomDelayFlux().doOnNext(System.out::println)
                .blockLast();
    }

    /*
    //private Flux<Long> randomDelayFlux() {
//        return Flux.create(sink -> {
//            sink.next(getRandomDelayMono());
//        });

        Flux<Mono<Long>> created  = Flux.create(sink -> {
           sink.next(getRandomDelayMono());
        });
        return created;

    }*/


    private Mono<Long> getRandomDelayMono() {
        int random = ThreadLocalRandom.current().nextInt(1,10);
        System.out.println("delay:" + random);
        Mono<Long> delay = Mono.delay(Duration.ofSeconds(random)).then(Mono.just(1L));
        return delay;
    }
}

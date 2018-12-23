package com.cognizant.labs.spring.flux.awesomespringflux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

//http://sinhamohit.com/writing/reactor-core-tutorial

public class FluxMonoTest {

    @Test
    public void empty() {
        Mono<String> emptyMono = Mono.empty();
        StepVerifier.create(emptyMono).verifyComplete();

        Flux<String> emptyFlux = Flux.empty();
        StepVerifier.create(emptyFlux).verifyComplete();
    }

    @Test
    public void initialized() {
        Mono<String> nonEmptyMono = Mono.just("joe");
        StepVerifier.create(nonEmptyMono).expectNext("joe").verifyComplete();

        Flux<String> fluxFromIterable = Flux.fromIterable(Arrays.asList("mike", "john", "hardy"));
        StepVerifier.create(fluxFromIterable).expectNext("mike", "john", "hardy").verifyComplete();
    }

    @Test
    public void zip() {
        Flux<String> firstNames = Flux.fromIterable(Arrays.asList("A", "B", "C"));
        Flux<String> lastNames = Flux.fromIterable(Arrays.asList("1", "2", "3"));

        Flux<String> zipped = Flux.zip(firstNames, lastNames)
                .map(a -> a.getT1() + "=" + a.getT2());

        StepVerifier.create(zipped).expectNext("A=1", "B=2", "C=3").verifyComplete();
    }

    @Test
    public void delay() {
        Flux<String> firstNames = Flux.fromIterable(Arrays.asList("A", "B", "C"));
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        Flux<String> firstNamesWithDelay = firstNames.zipWith(delay, (s, l) -> s);

        List list = Flux.zip(firstNames, firstNamesWithDelay)
                .log()
                .collectList()
                .block();

        list.forEach(System.out::println);
    }
}

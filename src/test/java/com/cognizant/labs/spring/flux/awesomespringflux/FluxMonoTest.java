package com.cognizant.labs.spring.flux.awesomespringflux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

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
}

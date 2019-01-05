package com.cognizant.labs.spring.flux.awesomespringflux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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

        // log let's you see the delayed execution
        // while the subsequent iteration is without delay
        List list = Flux.zip(firstNames, firstNamesWithDelay)
                .log()
                .collectList()
                .block();

        list.forEach(System.out::println);
    }

    @Test
    public void delay2() {
        Flux<String> firstNames = Flux.fromIterable(Arrays.asList("A", "B", "C"));
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        Flux<String> firstNamesWithDelay = firstNames.zipWith(delay, (s, l) -> s);
        // todo come back to this later
    }

    @Test
    public void range() {
        List range = Flux.range(1, 5)
                //.parallel(10)
                .delayElements(Duration.ofSeconds(3))
                .log()
                .collectList()
                .block();

                range.forEach(System.out::println);
    }

    @Test
    public void doOnNext() {

        // todo ... didn't work.. come back to this later
        Flux f = Flux.range(1, 5)
                //.parallel(10)
                .delayElements(Duration.ofSeconds(3))
                .doOnNext(System.out::println);
        f.subscribe();
    }

    @Test
    public void mergeFluxWithDelayFlux() {
        Flux<String> strs = Flux.just("A", "B", "C");
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));

        Flux<String> strsWithDelay = strs.zipWith(delay, (str, longDelay) -> str);

        strsWithDelay
                .collectList()
                .block()
                .forEach(System.out::println);

        StepVerifier.create(strsWithDelay)
                .thenConsumeWhile((e) -> true, System.out::println);
    }

    @Test
    public void mergeMonoWithDelayFlux() throws InterruptedException {
        Mono<String> strs = Mono.just("A");
        Mono<Long> delay = Mono.delay(Duration.ofSeconds(3));

        Mono<String> strsWithDelay = strs.zipWith(delay, (str, longDelay) -> str);

        strsWithDelay
                 .subscribe(System.out::println)
                ;
        // crude way of testing
        Thread.sleep(10000);
        StepVerifier.create(strsWithDelay)
                .thenConsumeWhile((e) -> true, System.out::println);
    }


    @Test
    public void verifyConsumeWithDelay() {
        Flux<Long> range = Flux.interval(Duration.ofSeconds(1)).take(3);

        StepVerifier.create(range)
                .expectNext(0L, 1L, 2L)
                .expectComplete()
                .verify();

        StepVerifier.create(range)
                .thenConsumeWhile((e) -> e.longValue() >= 0, System.out::println)
                .expectComplete()
                .verify();

    }

    @Test
    public void monoException() {
        Flux foo = Flux.just("foo", "bar").concatWith(Mono.error(new RuntimeException()));

        StepVerifier.create(foo)
                .expectNext("foo", "bar")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void expectNextMatches() {
        Flux foo = Flux.just("foo", "bar");
        Predicate<String> first = (e) -> e.equalsIgnoreCase("foo");
        Predicate<String> second = (e) -> e.equalsIgnoreCase("bar");
        StepVerifier.create(foo)
                .expectNextMatches(first)
                .consumeNextWith( System.out::println)
                .expectComplete()
                .verify();

    }
}

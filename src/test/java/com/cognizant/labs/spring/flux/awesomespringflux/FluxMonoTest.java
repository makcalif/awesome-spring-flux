package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Address;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Container;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.IntegerSupplier;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Person;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.naming.ServiceUnavailableException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

//http://sinhamohit.com/writing/reactor-core-tutorial

public class FluxMonoTest {

    @Test
    public void testFirstMono() {
        Mono<String> firstMono = Mono.just("A");
        firstMono.subscribe(System.out::println);
    }

    @Test
    public void monoWithDelay() throws InterruptedException {
        Mono<String> firstMono = Mono.just("A");
        Mono<Long> fiveSecDelay = Mono.delay(Duration.ofSeconds(5));

        BiFunction<String, Long, Long> combinator = (letter, delay ) -> delay;
        firstMono.zipWith(fiveSecDelay, combinator)
                .subscribe(System.out::println);

        Thread.sleep(6000);
    }

    @Test
    public void monoWithDelay_stepVerify()  {
        Mono<String> firstMono = Mono.just("A");
        Mono<Long> fiveSecDelay = Mono.delay(Duration.ofSeconds(5));

        Mono<String> monoWithDelay = firstMono.zipWith(fiveSecDelay, (letter, delay) -> letter);

        StepVerifier.create(monoWithDelay)
                .expectNext("B")
                .verifyComplete();
    }

    @Test
    public void testFirstFlux() {
        Flux<String> flux = Flux.fromIterable(Arrays.asList("A", "B", "C"));

        flux.subscribe(System.out::println);
    }

    @Test
    public void testFlatMap() {
        Person person1 = new Person("Eric", new Address("101",  "grand st"));
        Person person2 = new Person("Brenda", new Address("4000",  "canyon st"));

        Flux<Person> persons = Flux.fromIterable(Arrays.asList(person1, person2));
        Flux<Address> addresses = persons.flatMap(person -> Mono.just(person.getAddress()));
        addresses.subscribe(System.out::println);
    }

    @Test
    public void testProduceErrors() {
        Mono<Person> person1 = Mono.just(new Person("Eric", new Address("101",  "grand st")));
        Mono<Person> errorPerson = Mono.error(new RuntimeException("Person system down for maintenance. Try again later"));

        Flux<Person> persons = Flux.concat(person1, errorPerson);
        Flux<Address> addresses = persons.flatMap(person -> Mono.just(person.getAddress()));
        addresses.subscribe(System.out::println);
    }

    @Test
    public void castObjectToSpecifiMono() {
        Mono<Person> errorPerson = Mono.<Person>error(new RuntimeException("Person system down for maintenance. Try again later"));

    }

    @Test
    public void testHandleErrors() {
        Mono<Person> person1 = Mono.just(new Person("Eric", new Address("101",  "grand st")));
        Mono<Person> errorPerson = Mono.<Person>error(new RuntimeException("Person system down for maintenance. Try again later"));

        Flux<Person> persons = Flux.concat(person1, errorPerson);
        Flux<Address> addresses = persons
                .onErrorResume(RuntimeException.class   , e -> {
                    System.out.println(e);
                    return Mono.empty();
                })
                .flatMap(person -> Mono.just(person.getAddress()));
        addresses.subscribe(System.out::println);
    }

    @Test
    public void testHandleErrorsWithOnErrorResume()  {
        // work on this later
        ConnectableFlux<Integer> longStream = Flux.<Integer>create(longFluxSink -> {
            Flux.range(1, 100).subscribe(val -> {
                if (val < 10 || val >90) {
                    longFluxSink.next(val);
                    //} else throw new RuntimeException("Some error");
                } else {
                    longFluxSink.error(new ServiceUnavailableException("Downstream error"));  //next( Mono.error(new RuntimeException("some error"));
                }
            });
        }).publish();

        longStream
                .onErrorResume(ServiceUnavailableException.class   , e -> {
                    System.out.println(e);
                    return Mono.empty();
                })
                .subscribe(System.out::println);
        longStream.connect();

    }

    @Test
    public void testHandleErrorsWithOnErrorContinue() {
        // work on this later
        ConnectableFlux<Integer> longStream = Flux.<Integer>create(longFluxSink -> {
             Flux.range(1, 100).subscribe(val -> {
                 if (val < 10 || val >90) {
                     longFluxSink.next(val);
                 //} else throw new RuntimeException("Some error");
                 } else longFluxSink.error(new RuntimeException("Some error"));  //next( Mono.error(new RuntimeException("some error"));
             });
        }).publish();

        longStream
                .onErrorContinue((e) -> true, (e, obj) -> {
                    System.out.println("obj:" + obj);
                })
                .subscribe(System.out::println);
        longStream.connect();

    }

    @Test
    public void testHandleErrorsHotStreamWithOnErrorContinue() {
        // work on this later
        ConnectableFlux<Long> longStream = Flux.<Long>create(longFluxSink -> {
            while(true) {
                longFluxSink.next(System.currentTimeMillis());
            }
        }).publish();

        longStream.subscribe(System.out::println);

        longStream.connect();
    }

    @Test
    public void testCollectFlatMapThenSingle() {
        Flux<Integer> numbers = Flux.range(1, 20);

        Supplier<Container> containerSupplier = () -> {return new Container();};

        BiConsumer<Container, Integer  > biConsumer = (c, i) ->  c.accumulate(i);
        numbers.collect(containerSupplier, biConsumer)
                .flatMap( combined -> {
                    return Mono.justOrEmpty(combined.getList().stream().max(Integer::compareTo))
                            .map(max -> {
                                combined.setMax(max);
                                return combined;
                                }
                            );
                })
                .single()
                .subscribe(System.out::println);
    }


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

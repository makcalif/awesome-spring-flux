package com.cognizant.labs.spring.flux.awesomespringflux;


import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PublisherSubscriberTests {

    @Test
    public void testPublisher() {

        //Flux<Integer> intFlux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4,5));
        Flux<Integer> intFlux = Flux.fromStream(IntStream.range(0,10).boxed());
        // flux range is also available : Flux.range(1,10)
        intFlux.subscribe(System.out::println);
    }

    @Test
    public void testSubscriber() {
        Flux<Integer> intFlux = Flux.fromStream(IntStream.range(0, 10).boxed());

        Subscriber subscriber = new Subscriber() {
            private Subscription s;
            int onNextAmount = 0;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                System.out.println("on subscribe");
                s.request(2);
            }

            @Override
            public void onNext(Object o) {
                onNextAmount ++;
                if(onNextAmount % 2 == 0) {
                    System.out.println("onNextAmount: " + onNextAmount);
                    s.request(2);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("on error");
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("on complete");
            }
        };

        intFlux.log()
                .subscribe(subscriber);

    }

    @Test
    public void testInfiniteFlux() throws Exception {
        ConnectableFlux<Object> connectableFlux = Flux.create(fluxSink -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                fluxSink.next(System.currentTimeMillis());
            }
        })
        .publish();

        connectableFlux.log()
                .subscribe( t ->  {System.out.println("Subscirber 1:" + t);});

        connectableFlux.log()
                .subscribe( t ->  {System.out.println("Subscirber 2:" + t);});

        connectableFlux.connect();


    }

    @Test
    public void testInfiniteFluxWithDelayedSampling() throws Exception {
        ConnectableFlux<Object> connectableFlux = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        })
        .sample(Duration.ofSeconds(2))
        .publish();

        connectableFlux.log()
                .subscribe( t ->  {System.out.println("Subscirber 1:" + t);});

        connectableFlux.connect();

    }
    // add tests for delay plus step verifier in ppt
}

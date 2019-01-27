package com.cognizant.labs.spring.flux.awesomespringflux;


import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PublisherSubscriberTests {

    @Test
    public void testPublisher() {

        //Flux<Integer> intFlux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4,5));
        Flux<Integer> intFlux = Flux.fromStream(IntStream.range(0,10).boxed());
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
}

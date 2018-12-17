package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class FluxClientApplication implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //testBasic("/tweets");

        testBasic("/tweetstream");

    }

    private void testBasic(String endPoint) {
        WebClient webClient = WebClient.create("http://localhost:8080");
        Flux<Tweet> tweetsFlux = webClient.get()
                .uri(endPoint)
                .retrieve()
                .bodyToFlux(Tweet.class);

        tweetsFlux.subscribe(System.out::println);
    }
}

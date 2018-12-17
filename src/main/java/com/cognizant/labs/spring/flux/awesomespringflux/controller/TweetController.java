package com.cognizant.labs.spring.flux.awesomespringflux.controller;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class TweetController {

    @GetMapping ("/tweets")
    public Flux<Tweet> getAllTweets() {

        List<Tweet> tweets = getTweetsList();

        Tweet tweet1 = new Tweet( ); tweet1.setText("first tweet");
        Tweet tweet2 = new Tweet( ); tweet2.setText("first tweet");
        Tweet tweet3 = new Tweet( ); tweet3.setText("first tweet");

        Flux<Tweet> fluxTweets = Flux.just(tweet1, tweet2, tweet3).delayElements(Duration.ofSeconds(3));
        return fluxTweets;
    }

    @GetMapping(value = "/tweetstream", produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> streamAllTweets () {

        Flux<Tweet> tweetFlux = Flux.interval(Duration.ofSeconds(3))
                .map(tick -> new Tweet( tick.toString() ));

        return tweetFlux;
    }

    private List<Tweet> getTweetsList() {
        return null;
    }

//    @GetMapping ("/tweets/{id}")
//    public Mono<Tweet> getAllTweets() {
//
//    }
}

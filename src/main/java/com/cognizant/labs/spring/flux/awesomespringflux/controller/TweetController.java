package com.cognizant.labs.spring.flux.awesomespringflux.controller;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
public class TweetController {

    //http://reactivex.io/tutorials.html

    @GetMapping ("/tweets")
    public Flux<Tweet> getAllTweets() {

        List<Tweet> tweets = getTweetsList();

        Flux<Tweet> fluxTweets = Flux.just(tweets.get(0), tweets.get(1), tweets.get(2)).delayElements(Duration.ofSeconds(3));
        return fluxTweets;
    }

    @GetMapping(value = "/tweetstream", produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> streamAllTweets () {

        List<Tweet> tweets = getTweetsList();

        Flux<Tweet> tweetFlux = Flux.interval(Duration.ofSeconds(3))
                .map(tick -> new Tweet( tick.toString() ));

        return tweetFlux;
    }

    @GetMapping(value = "/tweetsrange", produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> streamTweetsRange () {

        List<Tweet> tweets = getTweetsList();

        Flux<Tweet> tweetFlux = Flux
                .fromIterable(tweets)
                //.delayElements(Duration.ofSeconds(3))
                .map(tick -> new Tweet( tick.toString() ));

        return tweetFlux;
    }

    private List<Tweet> getTweetsList() {
        List<Tweet> tweetList = Arrays.asList(
                new Tweet("first"),
                new Tweet("second"),
                new Tweet("third"));
        return tweetList;
    }

//    @GetMapping ("/tweets/{id}")
//    public Mono<Tweet> getAllTweets() {
//
//    }
}

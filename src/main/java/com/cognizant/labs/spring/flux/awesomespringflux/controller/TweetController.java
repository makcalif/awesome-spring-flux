package com.cognizant.labs.spring.flux.awesomespringflux.controller;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Allocation;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import com.cognizant.labs.spring.flux.awesomespringflux.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
public class TweetController {

    //http://reactivex.io/tutorials.html

    EmployeeService employeeService;

    @Autowired
    public TweetController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping ("/tweetsFakeDelay")
    public Flux<Tweet> getTweetsFakeDelay() {

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

    @GetMapping(value = "/nodelaytweetstream", produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> noDelayStreamAllTweets () {

        List<Tweet> tweets = getTweetsList();

        Flux<Tweet> tweetFlux = Flux.fromIterable(tweets)
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

    @GetMapping ("/employees/{id}")
    public Mono<Tweet> getEmployeeById(@PathVariable String id) {
        return this.employeeService.getById(id);
    }

    @GetMapping ("/employees/")
    public Flux<Tweet> getAllEmployees() {
        return this.employeeService.getAll();
    }

    @GetMapping(value = "/employee/{id}/allocations", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Allocation> getAllocations(@PathVariable String id) {
        return null; // employeeService.
    }
}

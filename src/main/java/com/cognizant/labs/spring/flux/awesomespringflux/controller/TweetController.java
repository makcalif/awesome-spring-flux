package com.cognizant.labs.spring.flux.awesomespringflux.controller;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Allocation;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import com.cognizant.labs.spring.flux.awesomespringflux.respository.UserRepository;
import com.cognizant.labs.spring.flux.awesomespringflux.service.TweetService;
import com.cognizant.labs.spring.flux.awesomespringflux.service.UserService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
public class TweetController {

    //http://reactivex.io/tutorials.html

    TweetService tweetService;
    UserService userService;

    @Autowired
    public TweetController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @GetMapping ("/tweetsFakeDelay")
    public Flux<Tweet> getTweetsFakeDelay() {

        List<Tweet> tweets = getTweetsList();

        Flux<Tweet> fluxTweets = Flux.just(tweets.get(0), tweets.get(1), tweets.get(2)).delayElements(Duration.ofSeconds(3));
        return fluxTweets;
    }

    @CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping(value = "/tweetstream/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> getTweetStream(@PathVariable String id) {
//        return tweetService.getById(id)
//                    .flatMap(user -> {
//                        System.out.println("User :" + user);
//                        return tweetService.streamTweets(user);
//                    });

        return null;
    }


    @GetMapping (value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping(value = "/users/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return this.userService.getUser(id);
    }

    @PostMapping(value = "/uploadUsers", consumes = "application/stream+json")
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<User> loadUsers(@RequestBody Flux<User> users) {
        return this.userService.insert(users);
    }
}

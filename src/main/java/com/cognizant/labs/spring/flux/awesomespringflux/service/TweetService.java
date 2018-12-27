package com.cognizant.labs.spring.flux.awesomespringflux.service;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import com.cognizant.labs.spring.flux.awesomespringflux.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class TweetService {

    UserRepository userRepository;

    @Autowired
    public TweetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public Flux<Tweet> getAll() {
//        return this.tweetRepository.findAll();
//    }

    public Mono<User> getById(String id) {
        return userRepository.findById(id);

    }

    public Flux<Tweet> streamTweets(User user) {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(3));
        Flux<Tweet> tweets = Flux.fromStream(
                Stream.generate( () -> new Tweet(user, (new Date()).toString()) ));

        return Flux.zip(interval, tweets).map(Tuple2::getT2);
    }
}

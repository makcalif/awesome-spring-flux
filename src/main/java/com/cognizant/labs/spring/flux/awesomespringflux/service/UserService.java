package com.cognizant.labs.spring.flux.awesomespringflux.service;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import com.cognizant.labs.spring.flux.awesomespringflux.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Mono<User> getUser(String id) {
        return this.userRepository.findById(id);
    }
}

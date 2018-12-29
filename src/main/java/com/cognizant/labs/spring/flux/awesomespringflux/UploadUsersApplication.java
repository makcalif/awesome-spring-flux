package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

public class UploadUsersApplication {
    private static Logger logger = LoggerFactory.getLogger(UploadUsersApplication.class);
    public static void main(String[] args) {
        WebClient client = WebClient.create("http://localhost:8080");

        UserFactory userFactory = new UserFactory();

        Flux<User> users = Flux.interval(Duration.ofSeconds(2)).map(i -> userFactory.getNewUser());

//        client.post()
//                .uri("/users")
//                .contentType(MediaType.APPLICATION_STREAM_JSON)
//                .syncBody(userFactory.getNewUser());

        client.post()
                .uri("/uploadUsers")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(users, User.class)
                .retrieve()
                .bodyToFlux(User.class)
                .doOnNext(user -> logger.debug("Uploaded user : {}", user))
                .blockLast();
    }


}


class UserFactory {
    public User getNewUser() {

        UUID id = UUID.randomUUID();
        String name = "random " + id;
        User user = new User(id.toString(), name);
        return user;
    }

}
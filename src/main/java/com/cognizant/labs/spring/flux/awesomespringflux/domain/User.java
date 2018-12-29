package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document (collection = "user")
@ToString
public class User {

    String id;
    String firstName;

    public User() {
    }

    public User(String id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }
}

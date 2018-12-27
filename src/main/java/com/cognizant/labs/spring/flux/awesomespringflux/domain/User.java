package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.*;

@Data
@Getter
@Setter

@ToString
public class User {

    String id;
    String firstName;

    public User(String id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }
}

package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString

public class Employee {

    @Id
    private String Id;

    public Employee(String id, String firstName) {
        Id = id;
        this.firstName = firstName;
    }

    String firstName;


}

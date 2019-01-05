package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
public class Employee {

    @Id
    private String Id;
    private String firstName;
    private String lastName;

//    public Employee(String id, String firstName) {
//        Id = id;
//        this.firstName = firstName;
//    }




}

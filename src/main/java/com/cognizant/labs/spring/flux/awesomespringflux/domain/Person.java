package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    String name;
    Address address;
}

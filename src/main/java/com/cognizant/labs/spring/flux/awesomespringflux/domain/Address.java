package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    Long id;
    String type;
    String number;
    String street;
}

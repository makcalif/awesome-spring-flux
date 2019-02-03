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

    public Address(String number, String street) {
        this.number = number;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "number='" + number + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}

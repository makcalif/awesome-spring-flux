package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Equipment {

    private Long id;
    private String name;
    private String type;
}

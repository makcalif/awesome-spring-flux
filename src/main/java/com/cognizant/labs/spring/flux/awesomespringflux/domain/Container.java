package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Container {
    Integer max;
    List<Integer> list = new ArrayList<>();
    public Integer accumulate(Integer i) {
        list.add(i);
        return 0;
    }
}

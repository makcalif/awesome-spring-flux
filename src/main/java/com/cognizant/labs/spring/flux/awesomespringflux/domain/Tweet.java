package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.*;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor //(access = AccessLevel.PRIVATE)
public class Tweet {

    //d@Getter
    @Setter
      String title;

    //@Getter
    @Setter
      String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

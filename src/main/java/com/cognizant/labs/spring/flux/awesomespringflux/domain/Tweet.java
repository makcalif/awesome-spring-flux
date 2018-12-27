package com.cognizant.labs.spring.flux.awesomespringflux.domain;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Tweet {

    User user;
    String title;
    String text;

    public Tweet(User user, String title) {
        this.user = user;
        this.title = title;
    }

    public Tweet(String title) {
        this.title = title;
    }

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

    @Override
    public String toString() {
        return "Tweet{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

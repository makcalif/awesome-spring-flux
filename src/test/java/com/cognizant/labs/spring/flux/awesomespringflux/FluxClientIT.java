package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient(timeout = "36000")
public class FluxClientIT {

    @Autowired
    private WebTestClient webTestClient;

    //WebClient webClient;

//    @Before
//    public void setup() {
//        webClient = WebClient.create("http://localhost:8080");
//    }

    @Test
    public void testTweetRange () {
//        WebTestClient.bindToServer()
//                .baseUrl("http://localhost:8080")
//                .build()
//                .get()
//                .uri("/tweetsrange")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody();
    }

    @Test
    public void testGetAllTweets () {
        webTestClient.get().uri("/tweetsrange")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Tweet.class);
    }

    @Test
    public void test2() {
        FluxExchangeResult<String> result = webTestClient.get().uri("/tweetsrange").accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .returnResult(String.class);

        Flux<String> intervalString = result.getResponseBody();

        StepVerifier.create(intervalString)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(1))
                .expectNextCount(0)
                .thenAwait(Duration.ofSeconds(1))
                .expectNextCount(1)
                .thenAwait(Duration.ofSeconds(1))
                .expectNextCount(2);
    }

    @Test
    public void test3() {
        webTestClient.get().uri("/tweetsrange").accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tweet.class)
                .hasSize(3);
                //.contains(new Tweet());
    }
}

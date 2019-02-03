package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Tweet;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ErrorHandlingTests {

    //Mono<Tweet> fallBackTweet = Mono.just( new Tweet("Fallback tweet"));
    Tweet fallBackTweet = new Tweet("Fallback tweet");
    @Test
    public void testExceptionHandling() {
        //Mono<Tweet> exceptionMono = getBogusMono(Mono.error(new IllegalStateException()));
        Mono<Tweet> exceptionMono = getBogusMono2();

        StepVerifier.create(exceptionMono)

                .expectNext(fallBackTweet)
                .expectComplete()
                .verify();

    }

    @Test
    public void testExceptionHandlingInline() {

        Mono<Tweet> erroredMono = Mono.error(new RuntimeException("Business Exception"));
        Mono<Tweet> erroredWithFallback =  erroredMono.onErrorResume(e -> Mono.just(fallBackTweet));

        StepVerifier.create(erroredWithFallback)
                .expectNext(fallBackTweet)
                .expectComplete()
                .verify();

    }

    private Mono<Tweet> getBogusMono(Mono<Tweet> mono) {
        return mono.onErrorResume(throwable -> throwable instanceof Exception,
                throwable -> Mono.just(fallBackTweet));
    }

    private Mono<Tweet> getBogusMono2( ) {
        Mono<Tweet> mono = Mono.error(new RuntimeException("Some business error"));
        return mono.onErrorResume(throwable -> throwable instanceof Exception,
                throwable -> Mono.just(fallBackTweet));
    }

}

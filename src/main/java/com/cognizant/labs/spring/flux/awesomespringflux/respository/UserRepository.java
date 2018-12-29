package com.cognizant.labs.spring.flux.awesomespringflux.respository;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    @Tailable
    Flux<User> findUsersBy();
}

package com.cognizant.labs.spring.flux.awesomespringflux.respository;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

}

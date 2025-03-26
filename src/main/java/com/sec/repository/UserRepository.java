package com.sec.repository;

import com.sec.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    public void registerUser(User user);

    public String userActivation(String code);

    public User findByActivation(String code);


}

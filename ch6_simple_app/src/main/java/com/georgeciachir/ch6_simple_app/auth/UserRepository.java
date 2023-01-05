package com.georgeciachir.ch6_simple_app.auth;

import com.georgeciachir.ch6_simple_app.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, IncompatibleClassChangeError> {

    Optional<User> findUserByUsername(String username);
}

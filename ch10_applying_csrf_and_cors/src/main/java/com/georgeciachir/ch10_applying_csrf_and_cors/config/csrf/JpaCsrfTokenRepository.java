package com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf;

import com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCsrfTokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findTokenByIdentifier(String integer);
}

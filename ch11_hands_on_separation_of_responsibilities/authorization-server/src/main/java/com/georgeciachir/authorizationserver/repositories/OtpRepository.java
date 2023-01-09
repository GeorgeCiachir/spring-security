package com.georgeciachir.authorizationserver.repositories;

import com.georgeciachir.authorizationserver.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findOtpByUsername(String username);
}

package com.georgeciachir.authorizationserver.controllers;

import com.georgeciachir.authorizationserver.entities.Otp;
import com.georgeciachir.authorizationserver.repositories.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * This should normally not exist.
 *
 * Because there is no mechanism to receive the generated otp code, this controller
 * just provides a way to retrieve it, without the need to go to the DB
 *
 * In the business-server project there is a collection of http requests to simulate the entire
 * flow, from user creation, to the user accessing a resource
 */
@RestController
public class CodeController {

    @Autowired
    private OtpRepository otpRepository;

    @GetMapping("/otp/user/{username}")
    public String getCode(@PathVariable String username) {
        return otpRepository.findOtpByUsername(username)
                .map(Otp::getCode)
                .orElse(null);
    }
}

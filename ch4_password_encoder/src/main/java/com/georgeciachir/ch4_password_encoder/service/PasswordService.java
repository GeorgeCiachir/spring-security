package com.georgeciachir.ch4_password_encoder.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PasswordService {

    private static final String DEFAULT_ENCODER = "bcrypt";

    // Checkout the PasswordEncoderFactories to see the encoders Spring uses
    private static final Map<String, PasswordEncoder> ENCODERS = Map.of(
            "noop", NoOpPasswordEncoder.getInstance(),
            "bcrypt", new BCryptPasswordEncoder(),
            "scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8()
    );

    public String encode(String password, String encoding) {
        String type = encoding != null ? encoding : DEFAULT_ENCODER;
        PasswordEncoder encoder = ENCODERS.get(type);
        return encoder.encode(password);
    }
}

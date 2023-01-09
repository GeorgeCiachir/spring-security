package com.georgeciachir.authorizationserver.services;


import com.georgeciachir.authorizationserver.entities.Otp;
import com.georgeciachir.authorizationserver.entities.User;
import com.georgeciachir.authorizationserver.repositories.OtpRepository;
import com.georgeciachir.authorizationserver.repositories.UserRepository;
import com.georgeciachir.authorizationserver.utils.GenerateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        userRepository.findUserByUsername(user.getUsername())
                .filter(u -> passwordEncoder.matches(user.getPassword(), u.getPassword()))
                .ifPresentOrElse(this::renewOtp, () -> {
                    throw new BadCredentialsException("Bad credentials.");
                });
    }

    public void validate(Otp otpToValidate) {
        otpRepository.findOtpByUsername(otpToValidate.getUsername())
                .filter(o -> otpToValidate.getCode().equals(o.getCode()))
                .orElseThrow(() -> new BadCredentialsException("Bad credentials."));
    }

    private void renewOtp(User u) {
        String code = GenerateCodeUtil.generateCode();

        otpRepository.findOtpByUsername(u.getUsername())
                .ifPresentOrElse(
                        o -> updateClientToken(o, code),
                        () -> createClientToken(u, code));
    }

    private void createClientToken(User u, String code) {
        Otp otp = new Otp();
        otp.setUsername(u.getUsername());
        otp.setCode(code);
        otpRepository.save(otp);
    }

    private void updateClientToken(Otp token, String newValue) {
        token.setCode(newValue);
    }
}

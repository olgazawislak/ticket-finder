package com.ticketfinder.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCommand {

    private String email;
    private String password;

    public boolean isEmailValid() {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public User toUser() {
        return new User(email, getHashedPassword());
    }

    private String getHashedPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}

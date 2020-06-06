package com.ticketfinder.domain.user;

import com.ticketfinder.exception.BadRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.ticketfinder.configuration.security.SecurityConfig.TOKEN_HEADER;

@Log4j2
@RestController
public class LoginController {

    private AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestBody CreateUserCommand createUserCommand) {
        log.info("Input: {}", createUserCommand.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(createUserCommand.getEmail(),
                createUserCommand.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (!authenticate.isAuthenticated()) {
            throw new BadRequestException("User don't authenticated");
        }
        String token = TokenGenerator.generateToken(createUserCommand.getEmail());

        return ResponseEntity.ok()
                .header(TOKEN_HEADER, token)
                .build();
    }
}

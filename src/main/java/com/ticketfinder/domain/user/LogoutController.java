package com.ticketfinder.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private JwtBlacklistRepository jwtBlacklistRepository;

    @Autowired
    public LogoutController(JwtBlacklistRepository jwtBlacklistRepository) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
    }

    @PostMapping("logout")
    public void logout(@RequestHeader("Authorization") String headerValue) {
        Jwt jwtHeaderValue = new Jwt(headerValue);
        jwtBlacklistRepository.insert(jwtHeaderValue);
    }
}

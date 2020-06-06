package com.ticketfinder.domain.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

import static com.ticketfinder.configuration.security.SecurityConfig.JWT_SECRET;

public class TokenGenerator {
    public static final String TOKEN_TYPE = "JWT";

    public static String generateToken(String username) {
        byte[] signingKey = JWT_SECRET.getBytes();

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey))
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
    }
}

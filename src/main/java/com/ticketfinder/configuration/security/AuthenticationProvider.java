package com.ticketfinder.configuration.security;

import com.ticketfinder.domain.user.JwtBlacklistRepository;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.ticketfinder.configuration.security.SecurityConfig.JWT_SECRET;

public class AuthenticationProvider {

    private JwtBlacklistRepository jwtBlacklistRepository;

    public AuthenticationProvider(JwtBlacklistRepository jwtBlacklistRepository) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        if (jwtBlacklistRepository.existsById(token)) {
            return null;
        }

        String username = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        if (StringUtils.isNotEmpty(username)) {
            return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        }
        return null;
    }
}


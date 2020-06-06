package com.ticketfinder.configuration.security;

import com.ticketfinder.domain.user.JwtBlacklistRepository;
import com.ticketfinder.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    public static final String JWT_SECRET = "E(H+MbQeThWmYq3t6w9z$C&F)J@NcRfUjXn2r4u7x!A%D*G-KaPdSgVkYp3s6v8y";
    public static final String TOKEN_HEADER = "Authorization";

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserRepositoryDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(JwtBlacklistRepository jwtBlacklistRepository) {
        return new AuthenticationProvider(jwtBlacklistRepository);
    }
}

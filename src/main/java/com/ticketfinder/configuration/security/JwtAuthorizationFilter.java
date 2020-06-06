package com.ticketfinder.configuration.security;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.ticketfinder.configuration.security.SecurityConfig.TOKEN_HEADER;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthenticationProvider authenticationProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationProvider authenticationProvider) {
        super(authenticationManager);
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) {
        String token = request.getHeader(TOKEN_HEADER);
        UsernamePasswordAuthenticationToken authentication = authenticationProvider.getAuthentication(token);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
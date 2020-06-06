package com.ticketfinder.configuration.security;

import com.ticketfinder.domain.user.JwtBlacklistRepository;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.ticketfinder.domain.user.TokenGenerator.generateToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class AuthenticationProviderTest {

    @Test
    public void authenticationWithEmptyTokenTest() {
        JwtBlacklistRepository jwtMockRepository = mock(JwtBlacklistRepository.class);
        AuthenticationProvider authenticationProvider = new AuthenticationProvider(jwtMockRepository);

        assertThat(authenticationProvider.getAuthentication("")).isNull();
    }

    @Test
    public void authenticationWithBlacklistedTokenTest() {
        JwtBlacklistRepository jwtMockRepository = mock(JwtBlacklistRepository.class);
        when(jwtMockRepository.existsById(anyString())).thenReturn(true);
        AuthenticationProvider authenticationProvider = new AuthenticationProvider(jwtMockRepository);

        assertThat(authenticationProvider.getAuthentication("not empty")).isNull();
    }


    @Test
    public void authenticationWithTokenTest() {
        JwtBlacklistRepository jwtMockRepository = mock(JwtBlacklistRepository.class);
        when(jwtMockRepository.existsById(anyString())).thenReturn(false);
        AuthenticationProvider authenticationProvider = new AuthenticationProvider(jwtMockRepository);
        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken("o@wp.pl",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("USER")));

        assertThat(authenticationProvider.getAuthentication(generateToken("o@wp.pl"))).isEqualTo(expected);
    }

    @Test
    public void authenticationWithEmptyUsernameTest() {
        JwtBlacklistRepository jwtMockRepository = mock(JwtBlacklistRepository.class);
        when(jwtMockRepository.existsById(anyString())).thenReturn(false);
        AuthenticationProvider authenticationProvider = new AuthenticationProvider(jwtMockRepository);

        assertThat(authenticationProvider.getAuthentication(generateToken(""))).isNull();
    }

}
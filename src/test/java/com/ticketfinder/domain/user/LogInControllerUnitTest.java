package com.ticketfinder.domain.user;

import com.github.javafaker.Faker;
import com.ticketfinder.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class LogInControllerUnitTest {

    private Faker faker = new Faker();

    @Test
    public void createUserWithIncorrectEmailTest() {
        CreateUserCommand createUserCommand = new CreateUserCommand("@gmail.pl",
                faker.dragonBall().character());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserController userController = new UserController(userRepositoryMock);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.createUser(createUserCommand))
                .withMessage("Incorrect e-mail address");
    }

    @Test
    public void logInTest() {
        CreateUserCommand createUserCommand = new CreateUserCommand("asap@wp.pl",
                faker.animal().name());
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class, RETURNS_DEEP_STUBS);
        Mockito.when(authenticationManager.authenticate(Mockito.any()).isAuthenticated()).thenReturn(true);

        LoginController loginController = new LoginController(authenticationManager);
        loginController.login(createUserCommand);
    }

    @Test
    public void logInWithWrongPasswordTest() {
        CreateUserCommand createUserWithWrongPasswordCommand = new CreateUserCommand("asap@wp.pl",
                faker.country().name());
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class, RETURNS_DEEP_STUBS);
        Mockito.when(authenticationManager.authenticate(Mockito.any()).isAuthenticated()).thenReturn(false);
        LoginController loginController = new LoginController(authenticationManager);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> loginController.login(createUserWithWrongPasswordCommand))
                .withMessage("User don't authenticated");
    }
}

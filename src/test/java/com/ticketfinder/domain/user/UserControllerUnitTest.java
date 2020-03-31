package com.ticketfinder.domain.user;

import com.github.javafaker.Faker;
import com.ticketfinder.exception.BadRequestException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserControllerUnitTest {
    private Faker faker = new Faker();

    @Test
    public void createUserTest() {
        CreateUserCommand createUserCommand = new CreateUserCommand("xyz@wp.pl",
                faker.country().name());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.existsById(createUserCommand.getEmail())).thenReturn(false);

        UserController userController = new UserController(userRepositoryMock);
        userController.createUser(createUserCommand);
    }

    @Test
    public void createUserWithAlreadyExistEmailTest() {
        CreateUserCommand createUserCommand = new CreateUserCommand("abc@wp.pl",
                faker.country().name());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.existsById(createUserCommand.getEmail())).thenReturn(true);
        UserController userController = new UserController(userRepositoryMock);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.createUser(createUserCommand))
                .withMessage("An account for this e-mail already exist");
    }

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
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.findById(createUserCommand.getEmail()))
                .thenReturn(Optional.of(createUserCommand.toUser()));

        UserController userController = new UserController(userRepositoryMock);
        userController.logIn(createUserCommand);
    }

    @Test
    public void logInWithWrongPasswordTest() {
        CreateUserCommand createUserCommand = new CreateUserCommand("asap@wp.pl",
                faker.animal().name());
        CreateUserCommand createUserWithWrongPasswordCommand = new CreateUserCommand("asap@wp.pl",
                faker.country().name());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.findById(Mockito.anyString()))
                .thenReturn(Optional.of(createUserCommand.toUser()));

        UserController userController = new UserController(userRepositoryMock);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.logIn(createUserWithWrongPasswordCommand))
                .withMessage("Incorrect e-mail or password");
    }
}

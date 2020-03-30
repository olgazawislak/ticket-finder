package com.ticketfinder.domain.user;

import com.github.javafaker.Faker;
import com.ticketfinder.exception.BadRequestException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserControllerUnitTest {

    @Test
    public void createUserTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("xyz@wp.pl",
                faker.country().toString());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.existsById(createUserCommand.getEmail())).thenReturn(false);

        UserController userController = new UserController(userRepositoryMock);
        userController.createUser(createUserCommand);
    }

    @Test
    public void validationOfUserTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("abc@wp.pl",
                faker.country().toString());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.existsById(createUserCommand.getEmail())).thenReturn(true);
        UserController userController = new UserController(userRepositoryMock);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.createUser(createUserCommand))
                .withMessage("An account for this e-mail already exist");
    }

    @Test
    public void logInWithoutErrorTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("asap@wp.pl",
                faker.animal().toString());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.findById(createUserCommand.getEmail()))
                .thenReturn(Optional.of(createUserCommand.toUser()));

        UserController userController = new UserController(userRepositoryMock);
        userController.logIn(createUserCommand);
    }

    @Test
    public void logInWithWrongPasswordTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("asap@wp.pl",
                faker.animal().toString());
        CreateUserCommand createUserWithWrongPasswordCommand = new CreateUserCommand("asap@wp.pl",
                faker.country().toString());
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.findById(createUserCommand.getEmail()))
                .thenReturn(Optional.of(createUserCommand.toUser()));

        UserController userController = new UserController(userRepositoryMock);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.logIn(createUserWithWrongPasswordCommand))
                .withMessage("Incorrect e-mail or password");
    }
}

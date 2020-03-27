package com.ticketfinder.domain.user;

import com.ticketfinder.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createUser(@RequestBody CreateUserCommand createUserCommand) {
        if (!createUserCommand.isEmailValid()) {
            throw new BadRequestException("Incorrect e-mail address");
        }
        userRepository.save(createUserCommand.toUser());
    }
}

package com.ticketfinder.domain.user;

import com.ticketfinder.exception.BadRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createUser(@RequestBody CreateUserCommand createUserCommand) {
        log.info("Input: {}", createUserCommand.getEmail());
        if (!createUserCommand.isEmailValid()) {
            throw new BadRequestException("Incorrect e-mail address");
        }
        if (userRepository.existsById(createUserCommand.getEmail())) {
            throw new BadRequestException("An account for this e-mail already exist");
        }
        userRepository.save(createUserCommand.toUser());
        log.info("Output: Empty");
    }
}

package com.ticketfinder.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.ticketfinder.configuration.security.SecurityConfig.TOKEN_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LogInControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void logInTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("baddog@gmail.com",
                faker.dog().name());
        userRepository.save(createUserCommand.toUser());
        String createUserCommandAsString = objectMapper.writeValueAsString(createUserCommand);

        mockMvc.perform(post("/login")
                .content(createUserCommandAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(header().exists(TOKEN_HEADER));
    }

    @SneakyThrows
    @Test
    public void wrongPasswordLogInTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("baddog@gmail.com",
                faker.dog().name());
        CreateUserCommand wrongCreateUserCommand = new CreateUserCommand("baddog@gmail.com",
                faker.cat().name());
        userRepository.save(createUserCommand.toUser());
        String wrongCreateUserCommandAsString = objectMapper.writeValueAsString(wrongCreateUserCommand);

        mockMvc.perform(post("/login")
                .content(wrongCreateUserCommandAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isForbidden());
    }
}

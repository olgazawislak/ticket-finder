package com.ticketfinder.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void createUserTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("gooddog@gmail.com",
                faker.chuckNorris().toString());
        String createUserCommandAsString = objectMapper.writeValueAsString(createUserCommand);

        mockMvc.perform(post("/users")
                .content(createUserCommandAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    public void createUserWithExistingEmailTest() {
        Faker faker = new Faker();
        CreateUserCommand createUserCommand = new CreateUserCommand("gooddog@gmail.com",
                faker.artist().toString());
        userRepository.save(createUserCommand.toUser());
        String createUserCommandAsString = objectMapper.writeValueAsString(createUserCommand);

        mockMvc.perform(post("/users")
                .content(createUserCommandAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isBadRequest());
    }
}

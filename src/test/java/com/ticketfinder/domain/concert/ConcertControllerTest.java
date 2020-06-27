package com.ticketfinder.domain.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.ticketfinder.exception.NotFoundException;
import java.util.Arrays;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class ConcertControllerTest {

    private final Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanRepository() {
        concertRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void getAllConcertsTest() {
        Concert concert = new ConcertBuilder().build();

        concertRepository.save(concert);

        String contentAsString = mockMvc.perform(get("/concerts"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Concert[] concerts = objectMapper.readValue(contentAsString, Concert[].class);
        assertThat(concerts).containsOnly(concert);
    }

    @SneakyThrows
    @Test
    void getAllConcertsPaginationTest() {
        Concert concert1 = new ConcertBuilder().build();
        Concert concert2 = new ConcertBuilder().build();
        Concert concert3 = new ConcertBuilder().build();

        concertRepository.saveAll(Arrays.asList(concert1,concert2,concert3));

        String contentAsString = mockMvc.perform(get("/concerts" + "?page=0&size=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Concert[] concerts = objectMapper.readValue(contentAsString, Concert[].class);
        assertThat(concerts).containsOnly(concert1,concert2);
    }

    @SneakyThrows
    @Test
    void getConcertsFromNextPageTest() {
        Concert concert1 = new ConcertBuilder().build();
        Concert concert2 = new ConcertBuilder().build();
        Concert concert3 = new ConcertBuilder().build();

        concertRepository.saveAll(Arrays.asList(concert1,concert2,concert3));

        String contentAsString = mockMvc.perform(get("/concerts" + "?page=1&size=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Concert[] concerts = objectMapper.readValue(contentAsString, Concert[].class);
        assertThat(concerts).containsOnly(concert3);
    }

    @SneakyThrows
    @Test
    void getConcertByIdTest() {
        Concert concert = new ConcertBuilder().build();
        concertRepository.save(concert);

        String contentAsString = mockMvc.perform(get("/concerts/" + concert.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Concert result = objectMapper.readValue(contentAsString, Concert.class);
        assertThat(result).isEqualTo(concert);
    }

    @SneakyThrows
    @Test
    void postConcertTest() {

        Concert concert = new ConcertBuilder().build();
        String concertAsString = objectMapper.writeValueAsString(concert);

        mockMvc.perform(post("/concerts").content(concertAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @SneakyThrows
    @Test
    void postConcertReservationTest() {
        ConcertParticipant concertParticipant = new ConcertParticipant(faker.name().firstName(), faker.name().lastName());
        Concert concert = new ConcertBuilder().build();
        String reservationAsString = objectMapper.writeValueAsString(concertParticipant);
        concertRepository.save(concert);

        mockMvc.perform(post("/concerts/" + concert.getId() + "/seats/" + concert.getSeats().get(0).getId())
                .content(reservationAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        Seat actualSeat = concertRepository.findById(concert.getId())
                .orElseThrow(() -> new NotFoundException("Concert doesn't exist"))
                .findSeat(concert.getSeats().get(0).getId());
        assertThat(actualSeat.isReserved()).isEqualTo(true);
        assertThat(actualSeat.getConcertParticipant()).isEqualToComparingFieldByField(concertParticipant);
    }
}

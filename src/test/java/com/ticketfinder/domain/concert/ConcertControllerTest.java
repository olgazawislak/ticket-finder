package com.ticketfinder.domain.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.ticketfinder.exception.NotFoundException;
import lombok.SneakyThrows;
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

    private Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void getAllConcertTest() {
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

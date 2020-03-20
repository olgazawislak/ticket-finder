package com.ticketfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketFinderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void getAllConcertTest() {
        Seat seat = Seat.createSeat("normal", 350);
        Concert concert = new Concert("0", "Bon Jovi", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Cracow", "Great Concert", Collections.singletonList(seat));
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
        Seat seat = Seat.createSeat("normal", 350);
        Concert concert = new Concert("1", "Abba", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Warsaw", "OK", Collections.singletonList(seat));
        concertRepository.save(concert);

        String contentAsString = mockMvc.perform(get("/concerts/1"))
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
        Seat seat = Seat.createSeat("normal", 350);
        Concert concert = new Concert("2", "Kat", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Nowa Huta", "OK", Collections.singletonList(seat));
        String concertAsString = objectMapper.writeValueAsString(concert);

        mockMvc.perform(post("/concerts").content(concertAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @SneakyThrows
    @Test
    void postConcertReservationTest() {
        Reservation reservation = new Reservation("Adam", "Malysz");
        Seat seat = Seat.createSeat("GA", 400);
        Concert concert = new Concert("3", "Katy Perry", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Alaska", "Nice Ice", Collections.singletonList(seat));
        String reservationAsString = objectMapper.writeValueAsString(reservation);

        concertRepository.save(concert);

        mockMvc.perform(post("/concerts/3/seats/" + seat.getId())
                .content(reservationAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        Seat actualSeat = concertRepository.findById("3").orElseThrow(NotFoundException::new).findSeat(seat.getId());
        assertThat(actualSeat.isReserved()).isEqualTo(true);
        assertThat(actualSeat.getReservation()).isEqualToComparingFieldByField(reservation);
    }
}

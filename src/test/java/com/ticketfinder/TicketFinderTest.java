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

    @Autowired
    private TicketFinderController ticketFinderController;

    @SneakyThrows
    @Test
    void getAllConcertTest() {
        Concert concert = new Concert("0", "Bon Jovi", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Cracow", "Great Concert");
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
        Concert concert = new Concert("1", "Abba", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Warsaw", "OK");
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
        Concert concert = new Concert("2", "Kat", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "Nowa Huta", "OK");
        String concertAsString = objectMapper.writeValueAsString(concert);

        mockMvc.perform(post("/concerts").content(concertAsString)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }
}

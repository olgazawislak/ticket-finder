package com.ticketfinder.domain.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class FindByTagsParameterizedTest {

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideConcertsAndTags")
    @DirtiesContext
    void findByTagsTest(List<Concert> concerts, MultiValueMap<String, String> tags, List<Concert> expectedConcerts) {
        concertRepository.saveAll(concerts);

        String concertsWithTagsAsString = mockMvc.perform(get("/concerts").queryParams(tags))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Concert[] concertsArray = objectMapper.readValue(concertsWithTagsAsString, Concert[].class);
        List<Concert> concertsWithTags = Arrays.asList(concertsArray);

        assertThat(concertsWithTags).hasSameElementsAs(expectedConcerts);
    }

    private static Stream<Arguments> provideConcertsAndTags() {
        List<String> tags = Arrays.asList("Metal", "Pogo");
        MultiValueMap<String, String> tagsMap = new LinkedMultiValueMap<>();
        tagsMap.put("tags", tags);

        Concert concertWithNoTags = new ConcertBuilder().setTags(Collections.emptyList()) .build();
        Concert concertWithNoExpectedTags = new ConcertBuilder().setTags(Collections.singletonList("Pop")).build();
        Concert concertWithTags = new ConcertBuilder().setTags(tags).build();
        Concert concertWithOneTag = new ConcertBuilder().setTags(Collections.singletonList("Metal")).build();
        Concert concertWithAdditionalTag = new ConcertBuilder().setTags(Arrays.asList("Metal", "Pogo", "Festival")).build();

        return Stream.of(
                Arguments.of(Arrays.asList(concertWithNoTags, concertWithTags, concertWithOneTag), tagsMap,
                        Collections.singletonList(concertWithTags)),
                Arguments.of(Arrays.asList(concertWithNoExpectedTags, concertWithAdditionalTag), tagsMap,
                        Collections.singletonList(concertWithAdditionalTag)),
                Arguments.of(Arrays.asList(concertWithTags, concertWithAdditionalTag), tagsMap,
                        Arrays.asList(concertWithTags, concertWithAdditionalTag))
        );
    }
}

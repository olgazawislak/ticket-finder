package com.ticketfinder.domain.concert;

import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

public class ConcertBuilder {
    private final Faker faker = new Faker();

    private String id = UUID.randomUUID().toString();
    private String name = faker.artist().name();
    private LocalDateTime date = LocalDateTime.now().truncatedTo(MINUTES);
    private String address = faker.address().fullAddress();
    private String description = faker.music().instrument();
    private List<String> tags = Collections.singletonList(faker.music().genre());
    private List<Seat> seats = Collections.singletonList(Seat.createSeat("normal", 350));

    public ConcertBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ConcertBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ConcertBuilder setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public ConcertBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public ConcertBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ConcertBuilder setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public ConcertBuilder setSeats(List<Seat> seats) {
        this.seats = seats;
        return this;
    }

    public Concert build() {
        return new Concert(id, name, date, address, description, tags, seats);
    }
}
package com.ticketfinder.domain.concert;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    private UUID id;
    private String description;
    private int price;
    private boolean reserved;
    private ConcertParticipant concertParticipant;

    public static Seat createSeat(String description, int price) {
        return new Seat(UUID.randomUUID(), description, price, false, new ConcertParticipant(null, null));
    }

    public void reserve(ConcertParticipant concertParticipant) {
        setReserved(true);
        setConcertParticipant(concertParticipant);
    }
}

package com.ticketfinder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    private UUID id;
    private String description;
    private int price;
    private boolean reserved;
    private Reservation reservation;

    public static Seat createSeat(String description, int price) {
        return new Seat(UUID.randomUUID(), description, price, false, new Reservation(null, null));
    }

    public void reserve(Reservation reservation) {
        setReserved(true);
        setReservation(reservation);
    }
}

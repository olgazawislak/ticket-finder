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
    private String name;
    private String surname;

    public static Seat createSeat(String description, int price) {
        return new Seat(UUID.randomUUID(), description, price, false, null, null);
    }

    public void reservation(String name, String surname) {
        setReserved(true);
        setName(name);
        setSurname(surname);
    }
}

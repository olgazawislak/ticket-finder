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
    private User user;

    public static Seat createSeat(String description, int price) {
        return new Seat(UUID.randomUUID(), description, price, false, new User(null, null));
    }

    public void reserve(User user) {
        setReserved(true);
        setUser(user);
    }
}

package com.ticketfinder.domain.concert;

import com.ticketfinder.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Concert {
    @Id
    private String id;
    private String name;
    private LocalDateTime date;
    private String address;
    private String description;
    private List<Seat> seats;

    public Seat findSeat(UUID id) {
        return getSeats().stream()
                .filter(seat -> seat.getId().equals(id))
                .findFirst().orElseThrow(NotFoundException::new);
    }
}

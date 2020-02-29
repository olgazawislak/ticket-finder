package com.ticketfinder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
class Concert {
    private String name;
    private LocalDateTime date;
    private String address;
    private String description;
}

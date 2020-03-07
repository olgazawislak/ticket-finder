package com.ticketfinder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Concert {
    @Id
    private String id;
    private String name;
    private LocalDateTime date;
    private String address;
    private String description;
}

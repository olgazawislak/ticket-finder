package com.example.TicketFinder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Concert {
    private String nameOfConcert;
    private LocalDate date;
    private String placeOfConcert;
    private String description;


}

package com.ticketfinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketFinderController {

    private ConcertRepository concertRepository;

    @Autowired
    public TicketFinderController(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @GetMapping("concerts/{id}")
    public Concert getConcert(@PathVariable String id) {
        return concertRepository.findById(id)
                .orElseThrow(ConcertNotFoundException::new);
    }

    @GetMapping("concerts")
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    @PostMapping("concerts")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postConcert(Concert concert) {
        concertRepository.insert(concert);
    }

}

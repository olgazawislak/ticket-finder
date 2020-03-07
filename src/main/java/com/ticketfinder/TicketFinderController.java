package com.ticketfinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketFinderController {

    private ConcertRepository concertRepository;

    @Autowired
    public TicketFinderController(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
}

    @GetMapping("concerts/{id}")
    public Concert getConcert(@PathVariable String id) throws ConcertNotFoundException {
        return concertRepository.findById(id)
                .orElseThrow(ConcertNotFoundException::new);
    }

    @GetMapping("concerts")
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    @PostMapping
    public void postConcert(Concert concert) {
        concertRepository.insert(concert);
    }

}

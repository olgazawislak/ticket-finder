package com.ticketfinder.domain.concert;

import com.ticketfinder.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("concerts")
@RestController
public class TicketFinderController {

    private ConcertRepository concertRepository;

    @Autowired
    public TicketFinderController(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @GetMapping("{id}")
    public Concert getConcert(@PathVariable String id) {
        return concertRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping()
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postConcert(@RequestBody Concert concert) {
        concertRepository.insert(concert);
    }

    @PostMapping("{concertId}/seats/{seatId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void postConcertReservation(@PathVariable String concertId,
                                       @PathVariable UUID seatId,
                                       @RequestBody ConcertParticipant concertParticipant) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(NotFoundException::new);
        concert.findSeat(seatId).reserve(concertParticipant);
        concertRepository.save(concert);
    }
}

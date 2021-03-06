package com.ticketfinder.domain.concert;

import com.ticketfinder.domain.user.JwtBlacklistRepository;
import com.ticketfinder.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequestMapping("concerts")
@RestController
public class ConcertController {

    private ConcertRepository concertRepository;
    private JwtBlacklistRepository jwtBlacklistRepository;

    @Autowired
    public ConcertController(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @GetMapping("{id}")
    public Concert getConcert(@PathVariable String id) {
        log.info("Input: {}", id);
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Concert doesn't exist"));
        log.info("Output {}", concert);
        return concert;
    }

    @GetMapping()
    public List<Concert> getAllConcerts(Pageable pageable) {
        log.info("Input: Empty");
        Page<Concert> concerts = concertRepository.findAll(pageable);
        log.info("Output: {}", concerts.getContent());
        return concerts.getContent();
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postConcert(@RequestBody Concert concert) {
        log.info("Input: {}", concert);
        concertRepository.insert(concert);
        log.info("Output: Empty");

    }

    @PostMapping("{concertId}/seats/{seatId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void postConcertReservation(@PathVariable String concertId,
                                       @PathVariable UUID seatId,
                                       @RequestBody ConcertParticipant concertParticipant) {
        log.info("Input: concert id {}, seat id {}, concert participant {}", concertId, seatId, concertParticipant);
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new NotFoundException("Concert doesn't exist"));
        concert.findSeat(seatId).reserve(concertParticipant);
        concertRepository.save(concert);
        log.info("Output: Empty");
    }

    @GetMapping(params = "tags")
    public List<Concert> findConcertsByTags(@RequestParam List<String> tags, Pageable pageable) {
        log.info("Input: {}", tags);
        List<Concert> concertsWithTags = getAllConcerts(pageable).stream()
                .filter(concert -> concert.getTags().containsAll(tags))
                .collect(Collectors.toList());
        log.info("Output: {}", concertsWithTags);
        return concertsWithTags;
    }
}

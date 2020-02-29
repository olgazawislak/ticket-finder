package com.example.TicketFinder;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConcertRepository extends MongoRepository<Concert, UUID> {
}

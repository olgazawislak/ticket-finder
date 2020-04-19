package com.ticketfinder.domain.concert;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends MongoRepository<Concert, String> {
}

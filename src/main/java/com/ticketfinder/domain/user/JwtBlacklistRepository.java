package com.ticketfinder.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlacklistRepository extends MongoRepository<Jwt, String> {
}

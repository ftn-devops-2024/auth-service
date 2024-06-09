package com.project.auth_service.repository;

import com.project.auth_service.model.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GuestRepository extends MongoRepository<Guest,String> {
}

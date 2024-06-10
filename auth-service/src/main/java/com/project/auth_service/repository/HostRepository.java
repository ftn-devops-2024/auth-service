package com.project.auth_service.repository;

import com.project.auth_service.model.Host;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HostRepository extends MongoRepository<Host,String> {
}

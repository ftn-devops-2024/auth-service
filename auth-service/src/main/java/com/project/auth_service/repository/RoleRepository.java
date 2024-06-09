package com.project.auth_service.repository;

import com.project.auth_service.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoleRepository extends MongoRepository<Role,Long> {

    List<Role> findByName(String name);
}

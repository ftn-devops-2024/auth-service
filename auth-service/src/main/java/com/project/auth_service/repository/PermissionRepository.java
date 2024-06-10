package com.project.auth_service.repository;

import com.project.auth_service.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionRepository  extends MongoRepository<Permission,String> {
}

package com.project.auth_service;

import com.project.auth_service.model.Permission;
import com.project.auth_service.model.Role;
import com.project.auth_service.repository.PermissionRepository;
import com.project.auth_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class Application implements CommandLineRunner {


	@Autowired
	private PermissionRepository permissionRepository;


	@Autowired
	private RoleRepository roleRepository;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Permission p2 = new Permission(2L, "dummy2");
		Permission pp2 = permissionRepository.save(p2);

		Role guest = roleRepository.findById(2L).orElse(null);
		guest.setPermissions(Set.of(pp2));
		roleRepository.save(guest);


	}
}

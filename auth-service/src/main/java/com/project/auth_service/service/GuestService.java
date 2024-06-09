package com.project.auth_service.service;

import com.project.auth_service.dto.RegisterUserDto;
import com.project.auth_service.model.Guest;
import com.project.auth_service.model.Role;
import com.project.auth_service.repository.GuestRepository;
import com.project.auth_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Guest save(RegisterUserDto dto){
        Guest g = new Guest();
        g.setEmail(dto.getMail());
        g.setName(dto.getName());
        g.setSurname(dto.getSurname());
        g.setEnabled(true);
        List<Role> roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findById(2L);  // 2 je guest
        role.ifPresent(roles::add);
        g.setRoles(roles);
        g.setPassword(passwordEncoder.encode(dto.getPassword()));
        return guestRepository.save(g);
    }
}

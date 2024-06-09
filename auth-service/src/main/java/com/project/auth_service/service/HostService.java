package com.project.auth_service.service;

import com.project.auth_service.dto.RegisterUserDto;
import com.project.auth_service.model.Guest;
import com.project.auth_service.model.Host;
import com.project.auth_service.model.Role;
import com.project.auth_service.repository.HostRepository;
import com.project.auth_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HostService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Host save(RegisterUserDto dto){
        Host h = new Host();
        h.setEmail(dto.getMail());
        h.setName(dto.getName());
        h.setSurname(dto.getSurname());
        h.setEnabled(true);
        List<Role> roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findById(1L);  // 1 ce biti host
        role.ifPresent(roles::add);
        h.setRoles(roles);
        h.setPassword(passwordEncoder.encode(dto.getPassword()));
        return hostRepository.save(h);
    }
}

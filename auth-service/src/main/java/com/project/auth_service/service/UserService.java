package com.project.auth_service.service;

import com.project.auth_service.dto.*;
import com.project.auth_service.exceptions.UnauthorizedException;
import com.project.auth_service.model.User;
import com.project.auth_service.repository.UserRepository;
import com.project.auth_service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HostService hostService;

    @Autowired
    private GuestService guestService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username);
    }

    public UserDTO register(RegisterUserDto dto)  {

        if (dto.getRole().equalsIgnoreCase("host")){
            return hostService.save(dto).toDto();
        } else if (dto.getRole().equalsIgnoreCase("guest")){
            return guestService.save(dto).toDto();
        } else {
            return null;
        }
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User authenticate(String token){
        String username = tokenUtils.getUsernameFromToken(token);
        User user = findByEmail(username);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        return user;
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User update(UserDTO dto) {
        User u = userRepository.findById(dto.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            u.setEmail(dto.getEmail());
        }
        if(dto.getName() != null && !dto.getName().isEmpty()) {
            u.setName(dto.getName());
        }
        if(dto.getSurname() != null && !dto.getSurname().isEmpty()) {
            u.setSurname(dto.getSurname());
        }
        if(dto.getAddress() != null && !dto.getAddress().isEmpty()) {
            u.setLocation(dto.getAddress());
        }
        u.setDeleted(dto.getDeleted());
        u.setHostResponse(dto.isHostResponse());
        u.setHostReview(dto.isHostReview());
        u.setReservationCanceled(dto.isReservationCanceled());
        u.setReservationRequest(dto.isReservationRequest());
        u.setAccommodationReview(dto.isAccommodationReview());
        if(!dto.getPassword().isEmpty()) u.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(u);
    }
}

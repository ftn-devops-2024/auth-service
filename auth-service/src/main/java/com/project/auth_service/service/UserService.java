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

import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HostService hostService;

    @Autowired
    private GuestService guestService;


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

    public void save(User user){
        userRepository.save(user);
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

}

package com.project.auth_service.controller;

import com.project.auth_service.dto.UserDTO;
import com.project.auth_service.exceptions.UnauthorizedException;
import com.project.auth_service.model.User;
import com.project.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/authorize")
public class AuthorizationController {

    private final UserService userService;

    @GetMapping("/host")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<UserDTO> authorizeHost(){
        System.out.println("Authorizing host");
        String authToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User user;
        try {
            user = userService.authenticate(authToken);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDTO dto = user.toDto();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/guest")
    @PreAuthorize("hasRole('ROLE_GUEST')")
    public ResponseEntity<UserDTO> authorizeGuest(){
        System.out.println("Authorizing guest");
        String authToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User user;
        try {
            user = userService.authenticate(authToken);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDTO dto = user.toDto();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/validateToken")
    @PreAuthorize("hasAnyRole('GUEST', 'HOST')")
    public ResponseEntity<UserDTO> validateToken(){
        String authToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User user;
        try {
            user = userService.authenticate(authToken);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDTO dto = user.toDto();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

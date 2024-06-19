package com.project.auth_service.controller;

import com.project.auth_service.dto.UserDTO;
import com.project.auth_service.model.User;
import com.project.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_HOST')")
    public ResponseEntity<UserDTO> get(@PathVariable String id){
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(user.toDto(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_HOST')")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto){
        try {
            User user = userService.update(dto);
            UserDTO response = user.toDto();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

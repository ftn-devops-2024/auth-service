package com.project.auth_service.controller;

import com.project.auth_service.dto.*;
import com.project.auth_service.exceptions.UnauthorizedException;
import com.project.auth_service.model.User;
import com.project.auth_service.repository.PermissionRepository;
import com.project.auth_service.service.LoggerService;
import com.project.auth_service.service.UserService;
import com.project.auth_service.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/auth")
public class AuthenticationController {

    private final UserService userService;

    private final TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PermissionRepository permissionRepository;


    private final LoggerService logger = new LoggerService(this.getClass());

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterUserDto dto) {
        logger.info(MessageFormat.format("Registering a new user with name {0}", dto.getName()));
        return userService.register(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto dto){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getMail(), dto.getPassword()));
        } catch(InternalAuthenticationServiceException | DisabledException e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if(user.getDeleted()){
            System.out.println("deleted");
            return ResponseEntity.badRequest().body(null);
        }
        String fingerprint = tokenUtils.generateFingerprint();
        String jwt = tokenUtils.generateToken(user, fingerprint);
        int expiresIn = tokenUtils.getExpiredIn();

        String cookie = "Fingerprint=" + fingerprint + "; SameSite=Strict; HttpOnly; Path=/; Secure";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie);
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(user.getUsername(), jwt, user.getRole(), expiresIn));
    }

    @GetMapping("/authorize/host")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<UserDTO> authorizeHost(){
        String authToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User user;
        try {
            user = userService.authorize(authToken);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDTO dto = user.toDto();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/authorize/guest")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<UserDTO> authorizeGuest(){
        String authToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User user;
        try {
            user = userService.authorize(authToken);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDTO dto = user.toDto();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

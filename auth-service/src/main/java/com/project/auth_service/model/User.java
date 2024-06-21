package com.project.auth_service.model;

import com.project.auth_service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document("users")
@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    private String email;
    private String name;
    private String surname;
    private String location;

    private String password;
    private Timestamp lastPasswordResetDate;
    private String loginPin;
    private Integer failedLoginAttempts = 0;
    private Boolean locked = false;
    private Boolean deleted = false;

    private boolean enabled = true;

    private boolean reservationRequest = true;
    private boolean reservationCanceled = true;
    private boolean hostReview = true;
    private boolean accommodationReview = true;
    private boolean hostResponse = true;

    public User(){}

    @DBRef
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public String getRole(){
        return roles.get(0).getName();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public UserDTO toDto() {
        return  new UserDTO(id, email, name, surname, location, getRole(), deleted, "",
                reservationRequest, reservationCanceled, hostReview, accommodationReview, hostResponse);
    }
}

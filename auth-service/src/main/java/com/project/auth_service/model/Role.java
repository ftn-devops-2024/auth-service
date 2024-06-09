package com.project.auth_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.data.annotation.Id;
import java.util.Set;

@Document("roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    Long id;

    String name;

    @DBRef
    @JsonIgnore
    private Set<Permission> permissions;

    @Override
    public String getAuthority() {
        return name;
    }}
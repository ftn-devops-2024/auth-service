package com.project.auth_service.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("guests")
@Getter
@Setter
public class Guest extends User {

    public Guest(){
        super();
    }

}

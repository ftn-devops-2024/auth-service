package com.project.auth_service.model;


import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("hosts")
@Getter
@Setter
public class Host extends User {
    public Host(){
        super();
    }
}

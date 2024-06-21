package com.project.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String email;
    private String name;
    private String surname;
    private String address;
    private String role;
    private Boolean deleted;
    private String password;

    private boolean reservationRequest;
    private boolean reservationCanceled;
    private boolean hostReview;
    private boolean accommodationReview;
    private boolean hostResponse;

}

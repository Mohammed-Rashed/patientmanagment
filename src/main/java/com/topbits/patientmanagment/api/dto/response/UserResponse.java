package com.topbits.patientmanagment.api.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean enabled;
}

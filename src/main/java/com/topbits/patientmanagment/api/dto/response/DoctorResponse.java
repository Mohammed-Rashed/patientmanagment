package com.topbits.patientmanagment.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialty;
    private String status;


}

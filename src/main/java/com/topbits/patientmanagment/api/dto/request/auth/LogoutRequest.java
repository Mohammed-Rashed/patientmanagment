package com.topbits.patientmanagment.api.dto.request.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutRequest {
    @NotBlank
    private String refreshToken;

}


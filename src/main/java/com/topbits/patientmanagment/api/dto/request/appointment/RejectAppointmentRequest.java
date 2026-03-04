package com.topbits.patientmanagment.api.dto.request.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectAppointmentRequest {
    @NotBlank(message = "reason is required")
    private String reason;
}
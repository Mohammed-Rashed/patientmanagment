package com.topbits.patientmanagment.api.dto.request.appointment;

import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateAppointmentRequest {
    @NotNull(message = "patientId is required")
    private Long patientId;

    @NotNull(message = "doctorId is required")
    private Long doctorId;

    @NotNull(message = "startTime is required")
    private LocalDateTime startTime;

    @NotNull(message = "endTime is required")
    private LocalDateTime endTime;

    private AppointmentStatus status;

    private String notes;
}

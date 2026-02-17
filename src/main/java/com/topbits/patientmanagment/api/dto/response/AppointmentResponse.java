package com.topbits.patientmanagment.api.dto.response;

import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;

    private Long patientId;
    private Long doctorId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private AppointmentStatus status;
    private String notes;
}

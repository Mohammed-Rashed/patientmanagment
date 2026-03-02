package com.topbits.patientmanagment.api.dto.general;

import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSearchCriteria {
    private AppointmentStatus status;
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
}

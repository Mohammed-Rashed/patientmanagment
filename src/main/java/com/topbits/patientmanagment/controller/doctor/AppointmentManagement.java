package com.topbits.patientmanagment.controller.doctor;

import com.topbits.patientmanagment.api.dto.gerneral.AppointmentSearchCriteria;
import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.service.AppointmentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/doctor/appointments")
@PreAuthorize("hasRole('DOCTOR')")
public class AppointmentManagement {
    AppointmentService appointmentService;
    public AppointmentManagement(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/requests")
    public PageResponse<AppointmentResponse> getAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDate date,
            Authentication auth,
            Pageable pageable
    ) {
        Long doctorId = Long.valueOf(auth.getName());
        AppointmentSearchCriteria criteria = AppointmentSearchCriteria.builder()
                .doctorId(doctorId)
                .status(status)
                .date(date)
                .build();
        return appointmentService.list(criteria, pageable);
    }

}

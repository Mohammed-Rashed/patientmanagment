package com.topbits.patientmanagment.controller.doctor;

import com.topbits.patientmanagment.api.dto.general.AppointmentSearchCriteria;
import com.topbits.patientmanagment.api.dto.request.appointment.RejectAppointmentRequest;
import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> cancel(@PathVariable Long id,
                                    @RequestBody
                                    @Valid
                                    RejectAppointmentRequest request) {
        AppointmentResponse appointmentResponse=appointmentService.reject(id, request.getReason());
        return ResponseEntity.ok(appointmentResponse);
    }

}

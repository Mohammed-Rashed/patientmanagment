package com.topbits.patientmanagment.controller;

import com.topbits.patientmanagment.api.dto.request.appointment.CreateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.request.appointment.UpdateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.DoctorResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.entity.Appointment;
import com.topbits.patientmanagment.service.AppointmentService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @GetMapping()
    public PageResponse<AppointmentResponse> getAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            Pageable pageable
    ) {
        return appointmentService.list( status, pageable);
    }
    @GetMapping("/{id}")
    public AppointmentResponse findById(@PathVariable Long id) {
        return appointmentService.findById(id);
    }
    @PostMapping()
    public AppointmentResponse create(@RequestBody CreateAppointmentRequest request) {
        return appointmentService.create(request);
    }
    @PutMapping("/{id}")
    public AppointmentResponse update(@PathVariable Long id,@RequestBody UpdateAppointmentRequest request) {
        return appointmentService.update(id,request);
    }
}

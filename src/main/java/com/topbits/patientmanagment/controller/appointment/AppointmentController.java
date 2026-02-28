package com.topbits.patientmanagment.controller.appointment;

import com.topbits.patientmanagment.api.dto.request.appointment.CreateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.request.appointment.UpdateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.response.AvailableSlotResponse;
import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public AppointmentResponse create(@RequestBody CreateAppointmentRequest request, Authentication authentication) {
        return appointmentService.create(request,authentication);
    }
    @PutMapping("/{id}")
    public AppointmentResponse update(@PathVariable Long id,@RequestBody UpdateAppointmentRequest request) {
        return appointmentService.update(id,request);
    }

    @PutMapping("/{id}/cancel")
    public AppointmentResponse update(@PathVariable Long id) {
        return appointmentService.cancel(id);
    }
    @GetMapping("/slots/{doctorId}")
    public List<AvailableSlotResponse> getDoctorSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return appointmentService.getDoctorSlots(doctorId, date);
    }
}

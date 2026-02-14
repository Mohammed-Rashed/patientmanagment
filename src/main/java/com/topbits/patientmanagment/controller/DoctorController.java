package com.topbits.patientmanagment.controller;

import com.topbits.patientmanagment.api.dto.request.doctor.CreateDoctorRequest;
import com.topbits.patientmanagment.api.dto.request.doctor.UpdateDoctorRequest;
import com.topbits.patientmanagment.api.dto.request.patient.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.request.patient.UpdatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.api.dto.response.DoctorResponse;
import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    @PostMapping()
    public DoctorResponse createPatient(@RequestBody @Valid CreateDoctorRequest request) {
        return doctorService.create(request);
    }

    @GetMapping("{id}")
    public DoctorResponse getPatientById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

    @PutMapping("/{id}")
    public DoctorResponse updatePatient(@PathVariable Long id, @RequestBody @Valid UpdateDoctorRequest request) {
        return doctorService.update(id, request);
    }

    @GetMapping()
    public PageResponse<DoctorResponse> getPatients(
                                             @RequestParam(required = false) String search,
                                             @RequestParam(required = false) DoctorStatus status,
                                             Pageable pageable
    ) {
        return doctorService.list(search, status, pageable);
    }

    @DeleteMapping("{id}")
    public void deletePatients(@PathVariable Long id) {
        doctorService.deleteById(id);
    }

}

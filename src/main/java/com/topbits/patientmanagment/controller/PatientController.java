package com.topbits.patientmanagment.controller;

import com.topbits.patientmanagment.api.dto.request.patient.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.request.patient.UpdatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import com.topbits.patientmanagment.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService patientService;
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @PostMapping()
    public PatientResponse createPatient(@RequestBody @Valid CreatePatientRequest request) {
        return patientService.create(request);
    }

    @GetMapping("{id}")
    public PatientResponse getPatientById(@PathVariable Long id) {
        return patientService.getById(id);
    }

    @PutMapping("/{id}")
    public PatientResponse updatePatient(@PathVariable Long id, @RequestBody @Valid UpdatePatientRequest request) {
        return patientService.update(id, request);
    }

    @GetMapping()
    public PageResponse<PatientResponse> getPatients(
                                             @RequestParam(required = false) String search,
                                             @RequestParam(required = false) PatientStatus status,
                                             Pageable pageable
    ) {
        return patientService.list(search, status, pageable);
    }

    @DeleteMapping("{id}")
    public void deletePatients(@PathVariable Long id) {
        patientService.deleteById(id);
    }

}

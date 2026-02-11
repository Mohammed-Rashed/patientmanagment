package com.topbits.patientmanagment.controller;

import com.topbits.patientmanagment.api.dto.request.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.service.PatientService;
import jakarta.validation.Valid;
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


}

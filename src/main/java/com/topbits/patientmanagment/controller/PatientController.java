package com.topbits.patientmanagment.controller;

import com.topbits.patientmanagment.api.dto.request.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.entity.Patient;
import com.topbits.patientmanagment.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

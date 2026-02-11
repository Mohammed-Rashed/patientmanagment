package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.entity.Patient;
import com.topbits.patientmanagment.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    public PatientResponse create(CreatePatientRequest request) {
        if(patientRepository.existsByEmailOrPhone(request.getEmail(), request.getPhone())) {
            throw new IllegalArgumentException("Email or Phone already exists");
        }
        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .status("ACTIVE")
                .build();
        Patient savedPatient = patientRepository.save(patient);
        return PatientResponse.builder()
                .id(savedPatient.getId())
                .firstName(savedPatient.getFirstName())
                .lastName(savedPatient.getLastName())
                .email(savedPatient.getEmail())
                .phone(savedPatient.getPhone())
                .dateOfBirth(savedPatient.getDateOfBirth())
                .status(savedPatient.getStatus())
                .build();
    }
}

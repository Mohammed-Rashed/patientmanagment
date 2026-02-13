package com.topbits.patientmanagment.service.mapper;

import com.topbits.patientmanagment.api.dto.request.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.request.UpdatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import com.topbits.patientmanagment.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public Patient toEntity(CreatePatientRequest req) {
        PatientStatus status = (req.getStatus() != null) ? req.getStatus() : PatientStatus.ACTIVE;

        return Patient.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .dateOfBirth(req.getDateOfBirth())
                .status(status)
                .build();
    }
    public void updateEntity(Patient patient, UpdatePatientRequest req) {
        patient.setFirstName(req.getFirstName());
        patient.setLastName(req.getLastName());
        patient.setEmail(req.getEmail());
        patient.setPhone(req.getPhone());
        patient.setDateOfBirth(req.getDateOfBirth());

        if (req.getStatus() != null) {
            patient.setStatus(req.getStatus());
        }
    }

    public PatientResponse toResponse(Patient p) {
        return PatientResponse.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .dateOfBirth(p.getDateOfBirth())
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .build();
    }
}

package com.topbits.patientmanagment.service.mapper;

import com.topbits.patientmanagment.api.dto.request.doctor.CreateDoctorRequest;
import com.topbits.patientmanagment.api.dto.request.doctor.UpdateDoctorRequest;
import com.topbits.patientmanagment.api.dto.response.DoctorResponse;
import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public Doctor toEntity(CreateDoctorRequest req) {
        DoctorStatus status = (req.getStatus() != null) ? req.getStatus() : DoctorStatus.ACTIVE;

        return Doctor.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .specialty(req.getSpecialty())
                .status(status)
                .build();
    }
    public void updateEntity(Doctor doctor, UpdateDoctorRequest req) {
        doctor.setFirstName(req.getFirstName());
        doctor.setLastName(req.getLastName());
        doctor.setEmail(req.getEmail());
        doctor.setPhone(req.getPhone());
        doctor.setSpecialty(req.getSpecialty());

        if (req.getStatus() != null) {
            doctor.setStatus(req.getStatus());
        }
    }

    public DoctorResponse toResponse(Doctor p) {
        return DoctorResponse.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .specialty(p.getSpecialty().name())
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .build();
    }
}

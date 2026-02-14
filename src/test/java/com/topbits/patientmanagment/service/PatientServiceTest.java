package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.patient.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import com.topbits.patientmanagment.entity.Patient;
import com.topbits.patientmanagment.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientService patientService;

    @Test
    void create_success() {
        CreatePatientRequest req = CreatePatientRequest.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed@example.com")
                .phone("0550000000")
                .dateOfBirth(LocalDate.of(1995, 1, 20))
                .status(PatientStatus.ACTIVE)
                .build();

        when(patientRepository.existsByEmailOrPhone(req.getEmail(), req.getPhone()))
                .thenReturn(false);

        Patient saved = Patient.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .dateOfBirth(req.getDateOfBirth())
                .status(req.getStatus())
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        PatientResponse res = patientService.create(req);

        assertEquals("Ahmed", res.getFirstName());
        assertEquals("Ali", res.getLastName());
        assertEquals("ahmed@example.com", res.getEmail());

        verify(patientRepository).existsByEmailOrPhone(req.getEmail(), req.getPhone());
        verify(patientRepository).save(any(Patient.class));
    }

}

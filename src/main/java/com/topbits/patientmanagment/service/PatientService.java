package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.patient.CreatePatientRequest;
import com.topbits.patientmanagment.api.dto.request.patient.UpdatePatientRequest;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.common.exception.ConflictException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.common.paging.PageMapper;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import com.topbits.patientmanagment.entity.Patient;
import com.topbits.patientmanagment.repository.PatientRepository;
import com.topbits.patientmanagment.repository.spec.PatientSpecifications;
import com.topbits.patientmanagment.service.mapper.PatientMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {

        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }
    @Transactional
    public PatientResponse create(CreatePatientRequest request) {
        if(patientRepository.existsByEmailOrPhone(request.getEmail(), request.getPhone())) {
            throw new ConflictException("Email or Phone already exists");
        }
        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .status(PatientStatus.ACTIVE)
                .build();
        Patient savedPatient = patientRepository.save(patient);
        return PatientResponse.builder()
                .id(savedPatient.getId())
                .firstName(savedPatient.getFirstName())
                .lastName(savedPatient.getLastName())
                .email(savedPatient.getEmail())
                .phone(savedPatient.getPhone())
                .dateOfBirth(savedPatient.getDateOfBirth())
                .status(String.valueOf(savedPatient.getStatus()))
                .build();
    }
    @Transactional
    public PatientResponse update(Long id,UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        if(patientRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ConflictException("Email already exists");
        }
        if(patientRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new ConflictException("Phone already exists");
        }
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getStatus() != null) {
            patient.setStatus(request.getStatus());
        }

        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponse(saved);

    }



    public PatientResponse getById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        return patientMapper.toResponse(patient);
    }

    public PageResponse<PatientResponse> list(String search, PatientStatus status, Pageable pageable) {


        Specification<Patient> spec =
                (root, query, cb) -> cb.conjunction();
        if (status != null) {
            spec = spec.and(PatientSpecifications.hasStatus(status));
        }

        if (search != null && !search.isBlank()) {
            spec = spec.and(PatientSpecifications.search(search.trim()));
        }

        Page<PatientResponse> page = patientRepository.findAll(spec, pageable).map(patientMapper::toResponse);
        return PageMapper.toPageResponse(page);
    }
    @Transactional
    public void deleteById(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new NotFoundException("Patient not found");
        }
        patientRepository.deleteById(id);
    }
}

package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.doctor.CreateDoctorRequest;
import com.topbits.patientmanagment.api.dto.request.doctor.UpdateDoctorRequest;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.api.dto.response.DoctorResponse;
import com.topbits.patientmanagment.common.exception.ConflictException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.common.paging.PageMapper;
import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.entity.Doctor;
import com.topbits.patientmanagment.repository.DoctorRepository;
import com.topbits.patientmanagment.repository.spec.DoctorSpecifications;
import com.topbits.patientmanagment.service.mapper.DoctorMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {

        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }
    @Transactional
    public DoctorResponse create(CreateDoctorRequest request) {
        if(doctorRepository.existsByEmailOrPhone(request.getEmail(), request.getPhone())) {
            throw new ConflictException("Email or Phone already exists");
        }
        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialty(request.getEspecialty())
                .status(DoctorStatus.ACTIVE)
                .build();
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorResponse.builder()
                .id(savedDoctor.getId())
                .firstName(savedDoctor.getFirstName())
                .lastName(savedDoctor.getLastName())
                .email(savedDoctor.getEmail())
                .phone(savedDoctor.getPhone())
                .specialty(String.valueOf(savedDoctor.getSpecialty()))
                .status(String.valueOf(savedDoctor.getStatus()))
                .build();
    }
    @Transactional
    public DoctorResponse update(Long id,UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        if(doctorRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ConflictException("Email already exists");
        }
        if(doctorRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new ConflictException("Phone already exists");
        }
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialty(request.getEspecialty());
        if (request.getStatus() != null) {
            doctor.setStatus(request.getStatus());
        }

        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toResponse(saved);

    }



    public DoctorResponse getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        return doctorMapper.toResponse(doctor);
    }

    public PageResponse<DoctorResponse> list(String search, DoctorStatus status, Pageable pageable) {


        Specification<Doctor> spec =
                (root, query, cb) -> cb.conjunction();
        if (status != null) {
            spec = spec.and(DoctorSpecifications.hasStatus(status));
        }

        if (search != null && !search.isBlank()) {
            spec = spec.and(DoctorSpecifications.search(search.trim()));
        }

        Page<DoctorResponse> page = doctorRepository.findAll(spec, pageable).map(doctorMapper::toResponse);
        return PageMapper.toPageResponse(page);
    }
    @Transactional
    public void deleteById(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new NotFoundException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }
}

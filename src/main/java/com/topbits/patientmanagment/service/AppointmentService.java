package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.appointment.CreateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.request.appointment.UpdateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.DoctorResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.common.exception.ConflictException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.common.paging.PageMapper;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.entity.Appointment;
import com.topbits.patientmanagment.entity.Doctor;
import com.topbits.patientmanagment.entity.Patient;
import com.topbits.patientmanagment.repository.AppointmentRepository;
import com.topbits.patientmanagment.repository.DoctorRepository;
import com.topbits.patientmanagment.repository.PatientRepository;
import com.topbits.patientmanagment.repository.spec.AppointmentSpecifications;
import com.topbits.patientmanagment.repository.spec.DoctorSpecifications;
import com.topbits.patientmanagment.service.mapper.AppointmentMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppointmentService {
    final private AppointmentRepository appointmentRepository;
    final private AppointmentMapper appointmentMapper;
    final private PatientRepository patientRepository;
    final private DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public AppointmentResponse findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        return appointmentMapper.toResponse(appointment);
    }
    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest createAppointmentRequest) {

        Patient patient = patientRepository.findById(createAppointmentRequest.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(createAppointmentRequest.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
        ensureDoctorNotDoubleBookedForCreate(
                doctor.getId(),
                createAppointmentRequest.getStartTime(),
                createAppointmentRequest.getEndTime()
        );

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(createAppointmentRequest.getStartTime())
                .endTime(createAppointmentRequest.getEndTime())
                .status(createAppointmentRequest.getStatus())
                .notes(createAppointmentRequest.getNotes())
                .build();
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }
    @Transactional
    public AppointmentResponse update(Long id,UpdateAppointmentRequest updateAppointmentRequest) {

        Appointment existAppointment= appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
        AppointmentResponse appointmentResponse = appointmentMapper.toResponse(existAppointment);
        ensureDoctorNotDoubleBookedForUpdate(
                appointmentResponse.getDoctorId(),
                updateAppointmentRequest.getStartTime(),
                updateAppointmentRequest.getEndTime(),
                id
        );

        existAppointment.setStartTime(updateAppointmentRequest.getStartTime());
        existAppointment.setEndTime(updateAppointmentRequest.getEndTime());
        existAppointment.setStatus(updateAppointmentRequest.getStatus());
        existAppointment.setNotes(updateAppointmentRequest.getNotes());

        return appointmentMapper.toResponse(appointmentRepository.save(existAppointment));
    }
    public void ensureDoctorNotDoubleBookedForCreate(Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {


        if(appointmentRepository.existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
                doctorId, endTime, startTime
        )) {

            throw new ConflictException("Doctor is already booked for the given time range");
        }
    }
    public void ensureDoctorNotDoubleBookedForUpdate(Long doctorId, LocalDateTime startTime, LocalDateTime endTime, Long appointmentId) {
        if(appointmentRepository.existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                doctorId, endTime, startTime, appointmentId
        )) {
            throw new ConflictException("Doctor is already booked for the given time range");
        }
    }
    public PageResponse<AppointmentResponse> list( AppointmentStatus status, Pageable pageable) {
        Specification<Appointment> spec =
                (root, query, cb) -> cb.conjunction();
        if (status != null) {
            spec = spec.and(AppointmentSpecifications.hasStatus(status));
        }

        Page<AppointmentResponse> page = appointmentRepository.findAll(spec, pageable).map(appointmentMapper::toResponse);
        return PageMapper.toPageResponse(page);
    }

}

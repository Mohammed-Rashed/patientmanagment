package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.appointment.CreateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.request.appointment.UpdateAppointmentRequest;
import com.topbits.patientmanagment.api.dto.response.AvailableSlotResponse;
import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.common.exception.ConflictException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.common.paging.PageMapper;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.entity.Appointment;
import com.topbits.patientmanagment.entity.Doctor;
import com.topbits.patientmanagment.entity.Patient;
import com.topbits.patientmanagment.repository.AppointmentRepository;
import com.topbits.patientmanagment.repository.DoctorRepository;
import com.topbits.patientmanagment.repository.PatientRepository;
import com.topbits.patientmanagment.repository.spec.AppointmentSpecifications;
import com.topbits.patientmanagment.service.mapper.AppointmentMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
    @Modifying
    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest createAppointmentRequest, Authentication authentication) {

//        createAppointmentRequest.setPatientId(Long.valueOf(authentication.getName()));
        ensureTimeAccurately(
                createAppointmentRequest.getStartTime(),
                createAppointmentRequest.getEndTime()
        );
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
                .status(AppointmentStatus.SCHEDULED)
                .notes(createAppointmentRequest.getNotes())
                .build();
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }
    @Transactional
    public AppointmentResponse update(Long id,UpdateAppointmentRequest updateAppointmentRequest) {
        ensureTimeAccurately(
                updateAppointmentRequest.getStartTime(),
                updateAppointmentRequest.getEndTime()
        );
        Appointment existAppointment= appointmentRepository.findByIdAndStatusNot(id,AppointmentStatus.COMPLETED).orElseThrow(() -> new NotFoundException("Appointment not found"));
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
    public void ensureTimeAccurately(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();

        if (startTime.isBefore(now)) {
            throw new ValidationException("Start time must be in the future");
        }

        if (!endTime.isAfter(startTime)) {
            throw new ValidationException("End time must be after start time");
        }

        long minutes = java.time.Duration.between(startTime, endTime).toMinutes();

        if (minutes < 15) {
            throw new ValidationException("Appointment must be at least 15 minutes long");
        }
        if (minutes > 15) {
            throw new ValidationException("Appointment must be 15 minutes long");
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

    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment existAppointment = appointmentRepository.findByIdAndStatusNot(id,AppointmentStatus.CANCELED).orElseThrow(() -> new NotFoundException("Appointment not found"));
        existAppointment.setStatus(AppointmentStatus.CANCELED);
        return appointmentMapper.toResponse(appointmentRepository.save(existAppointment));
    }

    public List<AvailableSlotResponse> getDoctorSlots(Long doctorId, LocalDate date) {
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        LocalDateTime windowStart = date.atTime(LocalTime.of(16, 0));
        LocalDateTime windowEnd = date.atTime(LocalTime.of(22, 0));
        Duration slotDuration = Duration.ofMinutes(15);

        List<Appointment> reservedAppointments = appointmentRepository
                .findByDoctorIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        doctorId,
                        AppointmentStatus.SCHEDULED,
                        windowEnd,
                        windowStart
                );

        List<AvailableSlotResponse> availableSlots = new ArrayList<>();
        for (
                LocalDateTime slotStart = windowStart; // init iteration variable
                !slotStart.plus(slotDuration).isAfter(windowEnd); // condition if start time + 15 mins after endtime
                slotStart = slotStart.plus(slotDuration) // increment by 15 mins

        )


        {
            LocalDateTime slotEnd = slotStart.plus(slotDuration);
            LocalDateTime currentSlotStart = slotStart;
            LocalDateTime currentSlotEnd = slotEnd;

            Optional<Appointment> matchedAppointment = reservedAppointments.stream()
                    .filter(appointment ->
                            currentSlotStart.isBefore(appointment.getEndTime())
                                    && currentSlotEnd.isAfter(appointment.getStartTime())
                    )
                    .findFirst();
            boolean isEmpty = matchedAppointment.isEmpty();
            availableSlots.add(
                    AvailableSlotResponse.builder()
                            .appointmentId(isEmpty?null:matchedAppointment.get().getId())
                            .startTime(slotStart)
                            .endTime(slotEnd)
                            .isAvailable(isEmpty)
                            .build()
            );
        }

        return availableSlots;
    }

}

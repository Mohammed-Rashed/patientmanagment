package com.topbits.patientmanagment.repository;

import com.topbits.patientmanagment.api.dto.response.AppointmentResponse;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.entity.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

//    PageResponse<AppointmentResponse> findByDoctorId(Long doctorId, Pageable pageable);

//    PageResponse<AppointmentResponse> findByPatientId(Long patientId);
    Optional<Appointment> findById(Long doctorId);

    boolean existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(Long doctorId, LocalDateTime endTime, LocalDateTime startTime, Long appointmentId);

    boolean existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(Long doctorId, LocalDateTime endTime, LocalDateTime startTime);

    Optional<Appointment> findByIdAndStatusNot(Long id, AppointmentStatus appointmentStatus);
}

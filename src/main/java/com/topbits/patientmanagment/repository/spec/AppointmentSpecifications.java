package com.topbits.patientmanagment.repository.spec;

import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentSpecifications {

    public static Specification<Appointment> hasStatus(AppointmentStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Appointment> hasDoctorId(Long doctorId) {
        return (root, query, cb) -> cb.equal(root.get("doctor").get("id"), doctorId);
    }
    public static Specification<Appointment> hasPatientId(Long patientId) {
        return (root, query, cb) -> cb.equal(root.get("patient").get("id"), patientId);
    }
    public static Specification<Appointment> hasDate(LocalDate date) {
        return (root, query, cb) -> {

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("startTime"), startOfDay),
                    cb.lessThan(root.get("startTime"), endOfDay)
            );
        };
    }
}

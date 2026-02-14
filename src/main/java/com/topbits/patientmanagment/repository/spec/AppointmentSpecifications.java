package com.topbits.patientmanagment.repository.spec;

import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import com.topbits.patientmanagment.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSpecifications {


    public static Specification<Appointment> hasStatus(AppointmentStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }



}

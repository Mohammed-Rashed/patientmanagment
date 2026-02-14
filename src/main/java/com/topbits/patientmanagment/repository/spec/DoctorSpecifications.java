package com.topbits.patientmanagment.repository.spec;

import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.entity.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecifications {


    public static Specification<Doctor> hasStatus(DoctorStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Doctor> search(String keyword) {
        return (root, query, cb) -> {
            String value = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), value),
                    cb.like(cb.lower(root.get("lastName")), value),
                    cb.like(cb.lower(root.get("email")), value)
            );
        };
    }

}

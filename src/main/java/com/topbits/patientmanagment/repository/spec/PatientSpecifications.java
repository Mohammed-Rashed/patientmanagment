package com.topbits.patientmanagment.repository.spec;

import com.topbits.patientmanagment.domain.enums.PatientStatus;
import com.topbits.patientmanagment.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecifications {


    public static Specification<Patient> hasStatus(PatientStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Patient> search(String keyword) {
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

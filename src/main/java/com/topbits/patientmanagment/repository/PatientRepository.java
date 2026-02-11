package com.topbits.patientmanagment.repository;

import com.topbits.patientmanagment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmailOrPhone(String email, String phone);
}

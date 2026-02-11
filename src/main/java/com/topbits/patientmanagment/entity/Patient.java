package com.topbits.patientmanagment.entity;

import com.topbits.patientmanagment.domain.entity.BaseEntity;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="patients")
public class Patient extends BaseEntity {
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PatientStatus status;
}

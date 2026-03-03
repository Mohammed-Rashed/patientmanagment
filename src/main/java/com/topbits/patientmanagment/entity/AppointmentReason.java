package com.topbits.patientmanagment.entity;

import com.topbits.patientmanagment.domain.entity.BaseEntity;
import com.topbits.patientmanagment.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointment_reasons")
public class AppointmentReason extends BaseEntity {


    @Column(name = "reason", length = 255, nullable = false)
    private String reason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;
}

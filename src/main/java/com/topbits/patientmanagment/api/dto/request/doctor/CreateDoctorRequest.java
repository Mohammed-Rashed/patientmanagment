package com.topbits.patientmanagment.api.dto.request.doctor;

import com.topbits.patientmanagment.domain.enums.DoctorSpecialty;
import com.topbits.patientmanagment.domain.enums.DoctorStatus;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDoctorRequest {
    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 30)
    private String phone;

    @NotNull
    private DoctorSpecialty especialty;

    private DoctorStatus status;
}

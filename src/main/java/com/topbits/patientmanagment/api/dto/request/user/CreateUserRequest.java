package com.topbits.patientmanagment.api.dto.request.user;

import com.topbits.patientmanagment.domain.enums.RoleName;
import com.topbits.patientmanagment.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String password;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 30)
    private String phone;

    private Set<RoleName> roles;

}

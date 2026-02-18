package com.topbits.patientmanagment.service.mapper;

import com.topbits.patientmanagment.api.dto.request.user.CreateUserRequest;
import com.topbits.patientmanagment.api.dto.request.patient.UpdatePatientRequest;
import com.topbits.patientmanagment.api.dto.request.user.UpdateUserRequest;
import com.topbits.patientmanagment.api.dto.response.UserResponse;
import com.topbits.patientmanagment.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(CreateUserRequest req) {

        return User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .enabled(true)
                .build();
    }
    public void updateEntity(User user, UpdateUserRequest req) {
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());

        if (req.getEnabled() != null) {
            user.setEnabled(req.getEnabled());
        }
    }

    public  UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .enabled(u.getEnabled())
                .build();
    }
}

package com.topbits.patientmanagment.controller;


import com.topbits.patientmanagment.api.dto.request.user.CreateUserRequest;
import com.topbits.patientmanagment.api.dto.request.user.UpdateUserRequest;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.api.dto.response.UserResponse;
import com.topbits.patientmanagment.domain.enums.PatientStatus;
import com.topbits.patientmanagment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping()
    public UserResponse createPatient(@RequestBody @Valid CreateUserRequest request) {
        return userService.create(request);
    }

    @GetMapping("{id}")
    public UserResponse getPatientById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updatePatient(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @GetMapping()
    public PageResponse<UserResponse> getPatients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean enabled,


            Pageable pageable
    ) {
        return userService.list(search, enabled, pageable);
    }

    @DeleteMapping("{id}")
    public void deletePatients(@PathVariable Long id) {
        userService.deleteById(id);
    }

}

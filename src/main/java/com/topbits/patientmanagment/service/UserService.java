package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.user.CreateUserRequest;
import com.topbits.patientmanagment.api.dto.request.user.UpdateUserRequest;
import com.topbits.patientmanagment.api.dto.response.PageResponse;
import com.topbits.patientmanagment.api.dto.response.UserResponse;
import com.topbits.patientmanagment.common.exception.ConflictException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.common.paging.PageMapper;
import com.topbits.patientmanagment.entity.Role;
import com.topbits.patientmanagment.entity.User;
import com.topbits.patientmanagment.repository.RoleRepository;
import com.topbits.patientmanagment.repository.UserRepository;
import com.topbits.patientmanagment.repository.spec.UserSpecifications;
import com.topbits.patientmanagment.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,UserMapper  userMapper,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if(userRepository.existsByEmailOrPhone(request.getEmail(), request.getPhone())) {
            throw new ConflictException("Email or Phone already exists");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .enabled(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        List<Role> roles = roleRepository.findByNameIn(request.getRoles());

        user.getRoles().addAll(roles);
        User savedUser = userRepository.save(user);
        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
//                .enabled(savedUser.gete)
                .build();
    }
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User patient = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ConflictException("Email already exists");
        }
        if(userRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new ConflictException("Phone already exists");
        }
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setEnabled(true);

        User saved = userRepository.save(patient);
        return userMapper.toResponse(saved);

    }



    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    public PageResponse<UserResponse> list(String search, Boolean enabled, Pageable pageable) {


        Specification<User> spec =
                (root, query, cb) -> cb.conjunction();
        if (enabled != null) {
            spec = spec.and(UserSpecifications.hasStatus(enabled));
        }

        if (search != null && !search.isBlank()) {
            spec = spec.and(UserSpecifications.search(search.trim()));
        }

        Page<UserResponse> page = userRepository.findAll(spec, pageable).map(userMapper::toResponse);
        return PageMapper.toPageResponse(page);
    }
    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}

package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.domain.enums.RoleName;
import com.topbits.patientmanagment.entity.Role;
import com.topbits.patientmanagment.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

     public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
     }

     public void createDefaultRoles() {
         this.createRoleIfNotExist("ROLE_ADMIN");
         this.createRoleIfNotExist("ROLE_DOCTOR");
         this.createRoleIfNotExist("ROLE_PATIENT");
     }

    public List<Role> findAll() {
        return roleRepository.findAll();
     }

     public void createRoleIfNotExist(String name) {
         if (!roleRepository.existsByName(RoleName.valueOf(name))) {
             Role role = new Role();
             role.setName(RoleName.valueOf(name));
             roleRepository.save(role);
         }
    }
}

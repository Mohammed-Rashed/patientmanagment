package com.topbits.patientmanagment.config;

import com.topbits.patientmanagment.service.RoleService;
import com.topbits.patientmanagment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpApp implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;



    @Override
    public void run(String... args) throws Exception {
        if(roleService.findAll().isEmpty()) {
            roleService.createDefaultRoles();
        }
    }

}

package com.topbits.patientmanagment.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthDebugController {

    @GetMapping("/me")
    public Map<String,Object> me(Authentication authentication){
        Map<String,Object> res =new HashMap<>();
        res.put("name",authentication.getName());
        res.put("roles",authentication.getAuthorities());
        res.put("authenticated", authentication.isAuthenticated());
        return res;

    }
}

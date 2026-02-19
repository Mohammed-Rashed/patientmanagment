package com.topbits.patientmanagment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig   {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        UserDetails doctor = User.builder()
                .username("doctor")
                .password("{noop}doctor123")
                .roles("DOCTOR")
                .build();

        UserDetails patient = User.builder()
                .username("patient")
                .password("{noop}patient123")
                .roles("PATIENT")
                .build();

        return new InMemoryUserDetailsManager(admin, doctor, patient);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/doctors/**").hasRole("DOCTOR")
                        .requestMatchers("/api/v1/patients/**").hasRole("PATIENT")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

package com.topbits.patientmanagment.entity;

import com.topbits.patientmanagment.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true, length = 120)
    private String name;
    @Column(unique = true, length = 120)
    private String email;

    @Column(unique = true, length = 25)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled = true;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}

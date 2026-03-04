package com.topbits.patientmanagment.repository;

import com.topbits.patientmanagment.domain.enums.RoleName;
import com.topbits.patientmanagment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);

    boolean existsByName(RoleName name);
    List<Role> findByNameIn(Set<RoleName> names);
}

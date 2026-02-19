package com.topbits.patientmanagment.repository;

import com.topbits.patientmanagment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmailOrPhone(String email, String phone);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByEmail(String email);

}

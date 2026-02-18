package com.topbits.patientmanagment.repository.spec;

import com.topbits.patientmanagment.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> hasStatus(boolean enabled) {
        return (root, query, cb) -> cb.equal(root.get("enabled"), enabled);
    }

    public static Specification<User> search(String keyword) {
        return (root, query, cb) -> {
            String value = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), value),
                    cb.like(cb.lower(root.get("phone")), value),
                    cb.like(cb.lower(root.get("email")), value)
            );
        };
    }
}

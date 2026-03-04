package com.topbits.patientmanagment.common.security;

import com.topbits.patientmanagment.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static boolean hasRole(Authentication auth, String role) {
        if (auth == null || auth.getAuthorities() == null) return false;

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(role));
    }

    public static Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Unauthenticated");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails ud) {
            String username = ud.getUsername();
            try {
                return Long.parseLong(username);
            } catch (NumberFormatException ex) {
                throw new UnauthorizedException("Invalid authenticated principal username (expected userId number)");
            }
        }
        if (principal instanceof Integer i) return i.longValue();
        if (principal instanceof String s) {
            try { return Long.parseLong(s.trim()); }
            catch (NumberFormatException ignored) {}
        }

        throw new UnauthorizedException("Cannot resolve user id from security context");
    }
}

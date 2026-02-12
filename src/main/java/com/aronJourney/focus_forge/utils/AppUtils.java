package com.aronJourney.focus_forge.utils;

import com.aronJourney.focus_forge.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AppUtils {
    public static User getCurrentUser() {
        return (User) Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }
}

package ru.isntrui.lb.queries;

import ru.isntrui.lb.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        int graduateYear,
        String building,
        LocalDateTime registeredOn,
        String tgUsername,
        String avatarUrl
) {
}

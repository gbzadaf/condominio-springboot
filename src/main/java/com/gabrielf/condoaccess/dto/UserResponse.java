package com.gabrielf.condoaccess.dto;

import com.gabrielf.condoaccess.domain.enums.UserRole;
import com.gabrielf.condoaccess.domain.enums.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt

) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );

    }
}

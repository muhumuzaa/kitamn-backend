package com.kitamn.backend.dto;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public class UserAccountDto {

    public record CreateUserRequest(
            @NotBlank @Email @Size(max=100) String email,
            @NotBlank @Size(min=8, max=100) String password,
            @NotBlank @Size(max=100) String firstName,
            @NotBlank @Size(max=100) String lastName
    ){}

    public record UpdateUserRequest(
            @Email @Size(max=100) String email,
            @Size(min=8, max=100) String password
    ){}

    public record UpdatePasswordRequest(
            @NotBlank @Size(min=8, max=100) String newPassword
    ) {}

    public record UserResponse(
            Long id,
            String email,
            String roles,
            Boolean enabled,
            String firstName,
            String lastName,


            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){}
}

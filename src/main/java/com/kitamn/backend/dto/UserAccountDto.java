package com.kitamn.backend.dto;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public class UserAccountDto {

    public record createUserRequest(
            @NotBlank @Email @Size(max=100) String email,
            @NotBlank @Size(min=8, max=100) String password,
            @NotBlank @Size(max=100) String firstName,
            @NotBlank @Size(max=100) String lastName
    ){}

    public record updateUserRequest(
            @Email @Size(max=100) String email,
            @Size(min=8, max=100) String password
    ){}

    public record userResponse(
            Long id,
            String email,
            String firstName,
            String lastName,
            Boolean enabled,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){}
}

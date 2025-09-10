package com.kitamn.backend.dto;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public class UserProfileDto {

    public record UpdateProfileRequest(
            @Size(max=100) String firstName,
            @Size(max=100) String lastName,
            @Size (max=30) @Pattern(regexp="^\\+[1-9]\\d{1,14}$") String phone,
            @Size(max=500) String avatarUrl,
            @Size(max=500) String bio

            //Never send @NotBlank on updates coz the user may not want to change that field.
    ){}

    public record ProfileResponse(
            Long id,
            String firstName,
            String lastName,
            String phone,
            String avatarUrl,
            String bio,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt

    ){}
}

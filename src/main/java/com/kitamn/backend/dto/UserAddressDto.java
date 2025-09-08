package com.kitamn.backend.dto;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public class UserAddressDto {

    public record createAddressRequest(
            @NotBlank @Size(max=100) String line1,
            @Size(max=100) String line2,
            @NotBlank @Size(max=100) String city,
            @Size(max=100) String stateRegion,
            @Size(max=100) String postalCode,
            @NotBlank @Size(max=100) String country,
            @Size(max=100) String label,
            Boolean isPrimary

    ){}

    public record updateAddressRequest(
            @Size(max=100) String line1,
            @Size(max=100) String line2,
            @Size(max=100) String city,
            @Size(max=100) String stateRegion,
            @Size(max=100) String postalCode,
            @Size(max=100) String country,
            @Size(max=100) String label,
            Boolean isPrimary
    ){}

    public record addressResponse(
            Long id,
            String line1,
            String line2,
            String city,
            String stateRegion,
            String postalCode,
            String country,
            String label,
            boolean isPrimary,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){}
}

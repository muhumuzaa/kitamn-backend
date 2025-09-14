package com.kitamn.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitamn.backend.dto.UserProfileDto.ProfileResponse;
import com.kitamn.backend.dto.UserProfileDto.UpdateProfileRequest;
import com.kitamn.backend.services.UserProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserProfileControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean UserProfileService profService;

    private ProfileResponse sampleProfile() {
        return new ProfileResponse(
                100L,
                "Alice",
                "Wonder",
                "+1-416-000-0000",
                "https://cdn.example.com/a.png",
                "Bio here",
                "123 King St",
                "Apt 4",
                "Toronto",
                "ON",
                "M5A 0V1",
                "Canada",
                OffsetDateTime.parse("2025-09-01T09:00:00Z"),
                OffsetDateTime.parse("2025-09-02T10:00:00Z")
        );
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId}/profiles -> 200 OK with profile")
    void getByUserId_shouldReturn200() throws Exception {
        Long userId = 10L;
        when(profService.findByUserId(userId)).thenReturn(sampleProfile());

        mvc.perform(get("/api/v1/users/{userId}/profiles", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.city").value("Toronto"));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{userId}/profiles -> 200 OK with updated profile")
    void update_shouldReturn200() throws Exception {
        Long userId = 10L;
        UpdateProfileRequest patch = new UpdateProfileRequest(
                "Alicia", "Wonder",
                "+1-416-111-2222",
                "https://cdn.example.com/a2.png",
                "Updated bio",
                null, null, "Vaughan", "ON", "L4K 0A1", "Canada", "HOME"
        );

        when(profService.update(ArgumentMatchers.any(UpdateProfileRequest.class), ArgumentMatchers.eq(userId)))
                .thenReturn(new ProfileResponse(
                        100L,
                        "Alicia", "Wonder",
                        "+1-416-111-2222",
                        "https://cdn.example.com/a2.png",
                        "Updated bio",
                        null, null, "Vaughan", "ON", "L4K 0A1", "Canada",
                        OffsetDateTime.parse("2025-09-01T09:00:00Z"),
                        OffsetDateTime.parse("2025-09-03T10:00:00Z")
                ));

        mvc.perform(patch("/api/v1/users/{userId}/profiles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alicia"))
                .andExpect(jsonPath("$.city").value("Vaughan"))
                .andExpect(jsonPath("$.bio").value("Updated bio"));
    }
}

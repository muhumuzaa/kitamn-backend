package com.kitamn.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitamn.backend.dto.UserAccountDto.CreateUserRequest;
import com.kitamn.backend.dto.UserAccountDto.UpdatePasswordRequest;
import com.kitamn.backend.dto.UserAccountDto.UpdateUserRequest;
import com.kitamn.backend.dto.UserAccountDto.UserResponse;
import com.kitamn.backend.services.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserAccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserAccountControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean
    UserAccountService userService;

    private UserResponse sampleUser(Long id, boolean enabled) {
        return new UserResponse(
                id,
                "alice"+id+"@example.com",
                "USER",
                enabled,
                "Alice",
                "Wonder",
                OffsetDateTime.parse("2025-09-01T10:15:30Z"),
                OffsetDateTime.parse("2025-09-02T11:00:00Z")
        );
    }

    @Test
    @DisplayName("POST /api/v1/users -> 200 OK with created user (two-step activation => enabled=false)")
    void register_shouldReturn200() throws Exception {
        CreateUserRequest req = new CreateUserRequest(
                "Alice@Example.com", "Password123!", "Alice", "Wonder"
        );

        when(userService.register(ArgumentMatchers.any(CreateUserRequest.class)))
                .thenReturn(sampleUser(1L, false));

        mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("alice1@example.com"))
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} -> 200 OK with user")
    void getById_shouldReturn200() throws Exception {
        when(userService.getById(10L)).thenReturn(sampleUser(10L, true));

        mvc.perform(get("/api/v1/users/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/users -> 200 OK with paged list")
    void list_shouldReturnPagedUsers() throws Exception {
        List<UserResponse> items = List.of(sampleUser(1L, true), sampleUser(2L, false));
        Page<UserResponse> page = new PageImpl<>(items, PageRequest.of(0, 20), 2);

        when(userService.list(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].enabled").value(false))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(2));

        // Optional: verify Pageable passed down
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(userService).list(captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(20, captor.getValue().getPageSize());
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} -> 200 OK with updated user")
    void update_shouldReturn200() throws Exception {
        Long id = 42L;
        UpdateUserRequest patch = new UpdateUserRequest("new@example.com", null);

        when(userService.update(eq(id), any(UpdateUserRequest.class)))
                .thenReturn(new UserResponse(
                        id,
                        "new@example.com",
                        "USER",
                        true,
                        "Alice",
                        "Wonder",
                        OffsetDateTime.parse("2025-09-01T10:15:30Z"),
                        OffsetDateTime.parse("2025-09-05T08:00:00Z")
                ));

        mvc.perform(patch("/api/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id}/password -> 204 No Content")
    void updatePassword_shouldReturn204() throws Exception {
        Long id = 7L;
        UpdatePasswordRequest body = new UpdatePasswordRequest("BrandNewP4ss!");

        doNothing().when(userService).changePassword(id, "BrandNewP4ss!");

        mvc.perform(patch("/api/v1/users/{id}/password", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} -> 204 No Content")
    void delete_shouldReturn204() throws Exception {
        doNothing().when(userService).delete(5L);

        mvc.perform(delete("/api/v1/users/{id}", 5L))
                .andExpect(status().isNoContent());
    }
}

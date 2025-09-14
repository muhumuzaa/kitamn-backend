package com.kitamn.backend.services;

import com.kitamn.backend.domain.UserProfile;
import com.kitamn.backend.dto.UserProfileDto.ProfileResponse;
import com.kitamn.backend.dto.UserProfileDto.UpdateProfileRequest;
import com.kitamn.backend.repos.UserAccountRepository;
import com.kitamn.backend.repos.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock private UserProfileRepository profiles;
    @Mock private UserAccountRepository users;

    private UserProfileService service;

    private final OffsetDateTime NOW = OffsetDateTime.parse("2025-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        // Explicitly wire the service with the mocks to avoid NPE
        service = new UserProfileService(profiles);
    }

    private UserProfile makeExistingProfile(Long id) {
        // If Lombok @Builder exists on UserProfile, you can use it.
        // Using builder for convenience; switch to setters if you prefer.
        return UserProfile.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .phone("+14165550100")
                .avatarUrl("https://old.example/avatar.jpg")
                .bio("Old bio")
                .line1("123 Old St")
                .line2("Unit 1")
                .city("OldCity")
                .stateRegion("OldState")
                .postalCode("OLD-123")
                .country("OldCountry")
                .createdAt(NOW)
                .updatedAt(NOW)
                .build();
    }

    // --- findByUserId ---

    @Test
    void findByUserId_returnsMappedResponse_whenProfileExists() {
        Long userId = 10L;
        UserProfile existing = makeExistingProfile(100L);
        when(profiles.findByUser_Id(userId)).thenReturn(Optional.of(existing));

        ProfileResponse res = service.findByUserId(userId);

        assertThat(res.id()).isEqualTo(100L);
        assertThat(res.firstName()).isEqualTo("John");
        assertThat(res.lastName()).isEqualTo("Doe");
        assertThat(res.phone()).isEqualTo("+14165550100");
        assertThat(res.avatarUrl()).isEqualTo("https://old.example/avatar.jpg");
        assertThat(res.bio()).isEqualTo("Old bio");
        assertThat(res.line1()).isEqualTo("123 Old St");
        assertThat(res.line2()).isEqualTo("Unit 1");
        assertThat(res.city()).isEqualTo("OldCity");
        assertThat(res.stateRegion()).isEqualTo("OldState");
        assertThat(res.postalCode()).isEqualTo("OLD-123");
        assertThat(res.country()).isEqualTo("OldCountry");
        assertThat(res.createdAt()).isEqualTo(NOW);
        assertThat(res.updatedAt()).isEqualTo(NOW);

        verify(profiles).findByUser_Id(userId);
        verifyNoMoreInteractions(profiles);
        verifyNoInteractions(users);
    }

    @Test
    void findByUserId_throws_whenProfileMissing() {
        Long userId = 77L;
        when(profiles.findByUser_Id(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByUserId(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Profile not found");

        verify(profiles).findByUser_Id(userId);
        verifyNoMoreInteractions(profiles);
        verifyNoInteractions(users);
    }

    // --- update ---

    @Test
    void update_updatesOnlyProvidedFields_andReturnsResponse_withoutSaving() {
        Long userId = 20L;
        UserProfile existing = makeExistingProfile(200L);
        when(profiles.findByUser_Id(userId)).thenReturn(Optional.of(existing));

        UpdateProfileRequest req = new UpdateProfileRequest(
                "Jane",                      // firstName (update)
                null,                        // lastName (unchanged)
                "+14165550123",              // phone (update)
                "https://new.example/a.jpg", // avatarUrl (update)
                "New bio",                   // bio (update)
                null,                        // line1 (unchanged)
                null,                        // line2 (unchanged)
                "Toronto",                   // city (update)
                null,                        // stateRegion (unchanged)
                "M5V 2T6",                   // postalCode (update)
                null,                        // country (unchanged)
                null                         // label (ignored by service)
        );

        ProfileResponse res = service.update(req, userId);

        assertThat(res.id()).isEqualTo(200L);
        assertThat(res.firstName()).isEqualTo("Jane");
        assertThat(res.lastName()).isEqualTo("Doe");
        assertThat(res.phone()).isEqualTo("+14165550123");
        assertThat(res.avatarUrl()).isEqualTo("https://new.example/a.jpg");
        assertThat(res.bio()).isEqualTo("New bio");
        assertThat(res.line1()).isEqualTo("123 Old St");
        assertThat(res.line2()).isEqualTo("Unit 1");
        assertThat(res.city()).isEqualTo("Toronto");
        assertThat(res.stateRegion()).isEqualTo("OldState");
        assertThat(res.postalCode()).isEqualTo("M5V 2T6");
        assertThat(res.country()).isEqualTo("OldCountry");
        assertThat(res.createdAt()).isEqualTo(NOW);
        assertThat(res.updatedAt()).isEqualTo(NOW);

        verify(profiles).findByUser_Id(userId);
        verify(profiles, never()).save(any(UserProfile.class)); // service doesn't save in update()
        verifyNoMoreInteractions(profiles);
        verifyNoInteractions(users);
    }

    @Test
    void update_throws_whenProfileMissing() {
        Long userId = 404L;
        when(profiles.findByUser_Id(userId)).thenReturn(Optional.empty());

        UpdateProfileRequest req = new UpdateProfileRequest(
                "Any", null, "+1111111111", null, null,
                null, null, null, null, null, null, null
        );

        assertThatThrownBy(() -> service.update(req, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Profile not found");

        verify(profiles).findByUser_Id(userId);
        verifyNoMoreInteractions(profiles);
        verifyNoInteractions(users);
    }
}

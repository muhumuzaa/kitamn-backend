package com.kitamn.backend.services;

import com.kitamn.backend.domain.*;
import com.kitamn.backend.dto.UserAccountDto.*;
import com.kitamn.backend.dto.UserAccountDto.UserResponse;
import com.kitamn.backend.repos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository users;
    @Mock private UserProfileRepository profiles;
    @Mock private PasswordEncoder encoder;

    @InjectMocks
    UserAccountService userService;

    //fixed timestamps for assertations
    private final OffsetDateTime NOW = OffsetDateTime.parse("2025-01-01T00:00:00Z");


//    @BeforeEach
//    void setUp(){
//        when(encoder.encode(anyString())).thenAnswer( inv -> "ENC("+ inv.getArgument(0, String.class) + ")");
//    }

    @Test
    void registerCreatesAccountAndProfile_whenEmailIsFree(){
        CreateUserRequest req = new CreateUserRequest("newuser@gmail.com", "pa$$w0rd!", "John", "Doe");
        when(users.existsByEmail("newuser@gmail.com")).thenReturn(false);

        //create a sample DB for UserAccount & Profile
        UserAccount savedAccount = new UserAccount().builder()
                .id(10L)
                .email("newuser@gmail.com")
                .password("ENC(pa$$w0rd!)")
                .roles("USER")
                .enabled(false)
                .createdAt(NOW)
                .updatedAt(NOW)
                .build();

        when(users.save(any(UserAccount.class))).thenReturn(savedAccount);

        UserProfile savedProfile = new UserProfile().builder()
                .id(100L)
                .firstName("John")
                .lastName("Doe").
                user(savedAccount).
                createdAt(NOW).
                updatedAt(NOW).
                build();

        when(profiles.save(any(UserProfile.class))).thenReturn(savedProfile);
//

        when(encoder.encode("pa$$w0rd!")).thenReturn("ENC(pa$$w0rd!)");

        //when
        UserResponse res = userService.register(req);

        //then
        //verify email normalized, password encoded, role is set
        ArgumentCaptor<UserAccount> uaCap = ArgumentCaptor.forClass(UserAccount.class);
        verify(users).save(uaCap.capture());


        assertThat(uaCap.getValue().getEmail()).isEqualTo("newuser@gmail.com");
        assertThat(uaCap.getValue().getPassword()).isEqualTo("ENC(pa$$w0rd!)");
        assertThat(uaCap.getValue().getRoles()).contains("USER");

        //profile created
        verify(profiles).save(any(UserProfile.class));

        //Response merged with profile names
        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.email()).isEqualTo("newuser@gmail.com");
        assertThat(res.firstName()).isEqualTo("John");
        assertThat(res.lastName()).isEqualTo("Doe");
    }

    @Test
    void signUp_throws_whenEmailAlreadyExists(){
        //given
        CreateUserRequest req = new CreateUserRequest("taken@example.com", "secret", "A", "B");

        when(users.existsByEmail("taken@example.com")).thenReturn(true);

        //when /then
        assertThatThrownBy(()-> userService.register(req))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Email already exists! Signup with a new email");

        verify(users, never()).save(any());
        verify(profiles, never()).save(any());
    }

    @Test
    void changePassword_updatesHarsh_andRejectsSamePassword(){
        //given
        Long userId = 5L;
        UserAccount existing = new UserAccount().builder()
                .id(userId)
                .email("user@example.com")
                .password("ENC(old123)")
                .roles("USER")
                .createdAt(NOW)
                .updatedAt(NOW)
                .build();

        when(users.findById(userId)).thenReturn(Optional.of(existing));

        //same password check
        when(encoder.matches(eq("old123"), eq("ENC(old123)"))).thenReturn(true);
        assertThatThrownBy(() ->userService.changePassword(userId, "old123")).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("You alraedy used this password before. You must use a new password!");

        //new password - accept and save
        when(encoder.matches(eq("new123"), eq("ENC(old123)"))).thenReturn(false);
        when(encoder.encode("new123")).thenReturn("ENC(new123)");
        userService.changePassword(userId, "new123");


        ArgumentCaptor<UserAccount> uaCap = ArgumentCaptor.forClass(UserAccount.class);
        verify(users).save(uaCap.capture());
        assertThat(uaCap.getValue().getPassword()).isEqualTo("ENC(new123)");
    }

    @Test
    void getById_returnsResponse_evenIfProfileMissing() {
        // given
        Long userId = 77L;
        UserAccount ua = UserAccount.builder()
                .id(userId)
                .email("noprof@example.com")
                .password("x")
                .roles("USER")
                .enabled(true)
                .createdAt(NOW)
                .updatedAt(NOW)
                .build();

        when(users.findById(userId)).thenReturn(Optional.of(ua));
        when(profiles.findByUser_Id(userId)).thenReturn(Optional.empty());

        // when
        UserResponse res = userService.getById(userId);

        // then
        assertThat(res.email()).isEqualTo("noprof@example.com");
        assertThat(res.firstName()).isNull();
        assertThat(res.lastName()).isNull();
    }


}

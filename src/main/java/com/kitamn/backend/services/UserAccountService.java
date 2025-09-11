package com.kitamn.backend.services;

import com.kitamn.backend.domain.UserAccount;
import com.kitamn.backend.domain.UserProfile;
import com.kitamn.backend.dto.UserAccountDto.*;
import com.kitamn.backend.repos.UserAccountRepository;
import com.kitamn.backend.repos.UserProfileRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.ResponseEntity.notFound;


@Service
@Transactional
public class UserAccountService {
    private UserAccountRepository users;
    private UserProfileRepository profiles;
    private PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository users, UserProfileRepository profiles, PasswordEncoder passwordEncoder){
        this.users = users;
        this.profiles = profiles;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(CreateUserRequest req){
        String email = req.email().trim().toLowerCase();
        if(users.existsByEmail(email)){
            throw new IllegalArgumentException("Email already exists! Signup with a new email");
        }
        UserAccount ua = new UserAccount();
        ua.setEmail(email);
        ua.setPassword(passwordEncoder.encode(req.password()));
        ua.setRoles("USER");
        ua.setEnabled(false);

        ua = users.save(ua);

        UserProfile p = new UserProfile();
        p.setFirstName(req.firstName());
        p.setLastName(req.lastName());
        p.setUser(ua);

        profiles.save(p);
        return toResponse(ua, p);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id){
        UserAccount ua = users.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));

        UserProfile p = profiles.findByUser_Id(id).orElse(null);

        return toResponse(ua, p);
    }


    @Transactional(readOnly=true)
    public Page<UserResponse> list(Pageable pageable){
        return users.findAll(pageable).map(ua -> toResponse(ua, profiles.findByUser_Id(ua.getId()).orElse(null)));
    }

    public UserResponse update(Long id, UpdateUserRequest req){
        var ua = users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: "+id));

        if(req.email() !=null && !req.email().isBlank()){
            var normEmail = req.email().trim().toLowerCase();
            if(!normEmail.equals(ua.getEmail()) && users.existsByEmail(normEmail)){
                throw new IllegalArgumentException("Email is already in use. Choose another email.");
            }
            ua.setEmail(normEmail);

        }
        if(req.password() != null && !req.password().isBlank()){
            ua.setPassword(passwordEncoder.encode(req.password()));
        }

        var profile = profiles.findByUser_Id(ua.getId()).orElse(null);

        return toResponse(ua, profile);
    }


    public void changePassword(Long id, String newPassword){
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank.");
        }
        var ua = users.findById(id).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!: "+id));

        if(passwordEncoder.matches(newPassword, ua.getPassword())){
            throw new IllegalArgumentException("You alraedy used this password before. You must use a new password!");
        }
        ua.setPassword(passwordEncoder.encode(newPassword));
        users.save(ua);
    }


    public void delete(Long id){
        if(!users.existsById(id)) throw new IllegalArgumentException("No user with id: "+id);

        users.deleteById(id);
    }






    public UserResponse toResponse(UserAccount ua, UserProfile p){
        return new UserResponse(
                ua.getId(),
                ua.getEmail(),
                ua.getRoles(),
                ua.isEnabled(),
                p ==null? null: p.getFirstName(),
                p==null? null: p.getLastName(),
                ua.getCreatedAt(),
                ua.getUpdatedAt());
    }

}

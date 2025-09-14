package com.kitamn.backend.controllers;

import com.kitamn.backend.dto.UserAccountDto.*;
import com.kitamn.backend.services.UserAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserAccountController{

    private final UserAccountService userService;

    public UserAccountController(UserAccountService userService){
        this.userService = userService;
    }

    //-------- CREATE - signup --. creates a user account and a profile
    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest req){
        var response = userService.register(req);
        return ResponseEntity.ok(response);
    }

    // -----------Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    //-------- get list of all users -> pageable ----
    @GetMapping()
    public ResponseEntity<Page<UserResponse>> list(@PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(userService.list(pageable));
    }

    //------------update -> email and password
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@Valid  @PathVariable Long id, @RequestBody UpdateUserRequest req){
        return ResponseEntity.ok(userService.update(id, req));
    }

    //DTO dedicated to password change
    public record UpdatePasswordRequest(@NotBlank String newPassword){}

    // ----------update password only ------------------
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@Valid @PathVariable Long id, @RequestBody UpdatePasswordRequest req){
        userService.changePassword(id, req.newPassword());
        return ResponseEntity.noContent().build();
    }


    // -----------delete -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
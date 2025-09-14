package com.kitamn.backend.controllers;

import com.kitamn.backend.dto.UserProfileDto.*;
import com.kitamn.backend.services.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/users/{userId}/profiles")
public class UserProfileController {

    private final UserProfileService profService;

    public UserProfileController(UserProfileService profService){
        this.profService = profService;
    }


    // ---------- Get a profile for a user --------------
    @GetMapping()
    public ResponseEntity<ProfileResponse> getByUser_Id(@Valid @PathVariable Long userId){
        return ResponseEntity.ok(profService.findByUserId(userId));
    }

    // --------- Upsert/Update profile (bio, avatar, etc) -------
    @PatchMapping()
    public ResponseEntity<ProfileResponse> update(@Valid @PathVariable Long userId, @RequestBody UpdateProfileRequest req){
        var res = profService.update(req, userId);
        return ResponseEntity.ok(res);
    }
}

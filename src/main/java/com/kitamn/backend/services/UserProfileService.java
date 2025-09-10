package com.kitamn.backend.services;

import com.kitamn.backend.domain.UserAccount;
import com.kitamn.backend.domain.UserProfile;
import com.kitamn.backend.dto.UserProfileDto.*;
import com.kitamn.backend.repos.UserAccountRepository;
import com.kitamn.backend.repos.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserProfileService {
    private UserProfileRepository profiles;
    private UserAccountRepository users;
    public UserProfileService(UserProfileRepository profiles, UserAccountRepository users){
        this.profiles = profiles;
        this.users = users;
    }


    @Transactional(readOnly=true)
    public ProfileResponse findByUserId(Long userId){
        UserProfile p = profiles.findByUser_Id(userId).orElseThrow(() -> new IllegalArgumentException("Profile not found for user_id: "+userId));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public ProfileResponse update(UpdateProfileRequest req, Long userId){
        UserProfile p = profiles.findByUser_Id(userId).orElseThrow(()-> new IllegalArgumentException("Profile not found for userId: "+userId));

        p.setFirstName(req.firstName() !=null? req.firstName(): p.getFirstName());
        p.setLastName(req.lastName() != null? req.lastName(): p.getLastName());
        p.setPhone(req.phone() !=null ? req.phone(): p.getPhone());
        p.setAvatarUrl(req.avatarUrl() !=null ? req.avatarUrl(): p.getAvatarUrl() );
        p.setBio(req.bio() != null ? req.bio() : p.getBio());

        return toResponse(p);
    }

    //helpers
    public ProfileResponse toResponse(UserProfile p){
        return new ProfileResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getPhone(), p.getAvatarUrl(), p.getBio(), p.getCreatedAt(), p.getUpdatedAt());
    }
}


package com.kitamn.backend.repos;

import com.kitamn.backend.domain.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

    Optional<UserProfile> findByUser_Id(Long userId);

    Page<UserProfile> searchByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            String firstNamePart,
            String lastNamePart,
            Pageable pageable
    );

}
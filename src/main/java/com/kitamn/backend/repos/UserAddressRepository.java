package com.kitamn.backend.repos;

import com.kitamn.backend.domain.UserAccount;
import com.kitamn.backend.domain.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    Page<UserAddress> findByUser_Id(Long userId, Pageable pageable);

    Optional<UserAddress> findByUser_IdAndIsPrimaryTrue(Long userId);
}

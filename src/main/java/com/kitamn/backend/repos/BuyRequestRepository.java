package com.kitamn.backend.repos;

import com.kitamn.backend.domain.BuyRequest;
import com.kitamn.backend.dto.ListingsDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyRequestRepository extends JpaRepository<BuyRequest, Long> {

    Page<BuyRequest> findAllByBuyer_Id(Long buyerId, Pageable pageable);
}

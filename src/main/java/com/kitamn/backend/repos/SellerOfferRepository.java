package com.kitamn.backend.repos;

import com.kitamn.backend.domain.SellerOffer;
import com.kitamn.backend.dto.ListingsDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerOfferRepository extends JpaRepository<SellerOffer, Long> {
    List<ListingsDto.SellerOfferResponse> findAllForBuyer(Long buyerId);
}

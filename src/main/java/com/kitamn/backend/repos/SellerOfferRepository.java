package com.kitamn.backend.repos;

import com.kitamn.backend.domain.SellerOffer;
import com.kitamn.backend.dto.ListingsDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerOfferRepository extends JpaRepository<SellerOffer, Long> {

    List<SellerOffer> findAllByBuyRequest_Id(Long requestId); //All offers for a specific request (property path: offer.buyRequest.id = ?).

    List<SellerOffer>findAllByBuyRequest_Buyer_Id(Long buyerId); //All offers made on requests owned by a given buyer (path: offer.buyRequest.buyer.id = ?).

    List<SellerOffer> findAllBySeller_Id(Long sellerId); // All offers created by a specific seller(path: offer.seller.id =?)

}

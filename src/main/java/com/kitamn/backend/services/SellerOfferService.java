package com.kitamn.backend.services;

import com.kitamn.backend.domain.BuyRequest;
import com.kitamn.backend.domain.SellerOffer;
import com.kitamn.backend.domain.UserAccount;
import com.kitamn.backend.dto.ListingsDto.*;
import com.kitamn.backend.repos.BuyRequestRepository;
import com.kitamn.backend.repos.SellerOfferRepository;
import com.kitamn.backend.repos.UserAccountRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class SellerOfferService {

    private SellerOfferRepository sellRepo;
    private UserAccountRepository userRepo;
    private BuyRequestRepository buyRepo;
    private UserAccount seller;

    public SellerOfferService(SellerOfferRepository sellRepo, BuyRequestRepository buyRepo, UserAccountRepository userRepo){
        this.sellRepo = sellRepo;
        this.buyRepo = buyRepo;
        this.userRepo = userRepo;
    }

    public SellerOfferResponse create(Long buyRequestId, CreateSellerOffer req){

        var buyRequest = buyRepo.findById(buyRequestId).orElseThrow(() -> new IllegalArgumentException("BuyRequest doesn't exist for id: "+buyRequestId));

        //TODO to be replaced by principal
        var sellerRef = userRepo.getReferenceById(req.sellerId());

        var newOffer = new SellerOffer().builder()
                .seller(sellerRef)
                .buyRequest(buyRequest)
                .message(req.message())
                .imageUrls(req.imageUrls())
                .build() ;
        newOffer = sellRepo.save(newOffer);

        return toSellerResponse(newOffer);
    }


    @Transactional(readOnly = true)
    public SellerOfferResponse GetById(Long sellOfferId){
        return toSellerResponse(sellRepo.findById(sellOfferId).orElseThrow(() -> new IllegalArgumentException("No offer found for id: "+sellOfferId)));

    }

    @Transactional(readOnly = true)
    public List<SellerOfferResponse> getOffersByBuyRequest_Id(Long requestId){
        return sellRepo.findAllByBuyRequest_Id(requestId).stream().map(this::toSellerResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<SellerOfferResponse> getAllOffersBySeller_Id(Long sellerId){
        return sellRepo.findAllBySeller_Id(sellerId).stream().map(this::toSellerResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<SellerOfferResponse> listOffersByBuyer(Long buyerId) {
        return sellRepo.findAllByBuyRequest_Buyer_Id(buyerId).stream()
                .map(this::toSellerResponse)
                .toList();
    }

    public SellerOfferResponse update(UpdateSellerOffer req, Long id, UserAccount seller){
        var offer = sellRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No sell offer found for id: "+id));

        if(!offer.getSeller().getId().equals(seller.getId())) throw new IllegalArgumentException("User not authorized to make this update");
        var update = new SellerOffer();

        update.setMessage(req.message() !=null ? req.message(): offer.getMessage());
        update.setImageUrls(req.imageUrls() !=null ? req.imageUrls(): offer.getImageUrls());

        return toSellerResponse(update);

    }

    //Delete the offer as the offer owner
    @Transactional
    public void deleteAsOwner(Long offerId, Long sellerId /*TODO update when auth is created */){
        var offer = sellRepo.findById(offerId).orElseThrow(() -> new IllegalArgumentException("Seller of does not exisit for id: "+offerId));

        if(!offer.getSeller().getId().equals(sellerId)) throw new IllegalArgumentException("You cannot delete this offer since you dont own it");
        sellRepo.delete(offer);
    }

    //the request owner can also delete the offer
    @Transactional
    public void deleteAsRequestOwner(Long offerId, Long buyerId, Long requestId){
        var offer = sellRepo.findById(offerId).orElseThrow(()-> new IllegalArgumentException("Offer doesnot exist for id: "+offerId));

        if(!offer.getBuyRequest().getId().equals(requestId)) throw new IllegalArgumentException("This offer doesnot belong to this Buy Request.");

        if(!offer.getBuyRequest().getBuyer().getId().equals(buyerId)) throw new IllegalArgumentException("This thread/request is not owned by you. You cant delete this offer");
    }





    // ----------------------------- Helpers --------------------------------------


    public SellerOfferResponse toSellerResponse(SellerOffer offer){
        return new SellerOfferResponse(
                offer.getId(),
                offer.getSeller() ==null ? null: offer.getSeller().getId(),
                offer.getBuyRequest() ==null ? null: offer.getBuyRequest().getId(),
                offer.getMessage(),
                offer.getStatus(),
                offer.getImageUrls(),
                offer.getCreatedAt(),
                offer.getUpdatedAt()
        );
    }
}

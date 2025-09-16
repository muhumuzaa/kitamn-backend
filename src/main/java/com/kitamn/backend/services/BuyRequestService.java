package com.kitamn.backend.services;

import com.kitamn.backend.domain.BuyRequest;
import com.kitamn.backend.domain.UserAccount;
import com.kitamn.backend.dto.ContentStatus;
import com.kitamn.backend.dto.ListingsDto.*;
import com.kitamn.backend.repos.BuyRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BuyRequestService {

    private BuyRequestRepository buyRepo;
    private UserAccountService users;

    public BuyRequestService(BuyRequestRepository buyRepo, UserAccountService users){
        this.buyRepo = buyRepo;
        this.users =users;
    }


    //----------------- create a buy request -----------------------------------

    public BuyRequestResponse create(CreateBuyRequest req, UserAccount buyer){

            BuyRequest newBuy = new BuyRequest().builder()
                    .buyer(buyer)
                    .title(req.title())
                    .description(req.description())
                    .category(req.category())
                    .buyerPrice(req.buyerPrice())
                    .currency(req.currency() == null ? "CAD" : req.currency().trim().toUpperCase())
                    .status(ContentStatus.RequestStatus.OPEN).
                    imageUrls(req.imageUrls() == null? List.of(): new ArrayList<>(req.imageUrls())).
                    build();


            newBuy = buyRepo.save(newBuy);

        return toBuyResponse(newBuy);
    }

    //----------------- find all buy requests -----------------------------------
    @Transactional(readOnly =true)
    public Page<BuyRequestResponse> getAll(Pageable pageable){
        return buyRepo.findAll(pageable).map(this::toBuyResponse);
    }


    //----------------- find buy requests by Id -----------------------------------
    @Transactional(readOnly = true)
    public Optional<BuyRequestResponse> getById(Long id){
        var req = buyRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No buy requests with id: "+id));
        return Optional.ofNullable(toBuyResponse(req));
    }



    //----------------- update buy request -----------------------------------

    public BuyRequestResponse update(Long id, UpdateBuyRequest req){
       var dbBuy = buyRepo.findById(id).orElseThrow(() ->new IllegalArgumentException("The Request id: "+id +" you want to update does not exist." +id));

        var toUpdate = new BuyRequest();

        toUpdate.setTitle(req.title()!= null? req.title(): dbBuy.getTitle());
        toUpdate.setDescription(req.description()!= null? req.description(): dbBuy.getDescription());
        toUpdate.setCategory(req.category()!= null? req.category(): dbBuy.getCategory());
        toUpdate.setBuyerPrice(req.buyerPrice()!= null? req.buyerPrice(): dbBuy.getBuyerPrice());
        if(req.imageUrls() != null) toUpdate.setImageUrls(new ArrayList<>(req.imageUrls()));

        return toBuyResponse(toUpdate);
    }

    public void delete(Long id){
        if(!buyRepo.existsById(id)) throw new IllegalArgumentException("The record doesnt exist for id: "+id);

        buyRepo.deleteById(id);
    }





    // ----------- Helpers ------------------------------

    public BuyRequestResponse toBuyResponse(BuyRequest req){
        return new BuyRequestResponse(
                req.getId(),
                req.getBuyer()== null? null: req.getBuyer().getId(),
                req.getTitle(),
                req.getDescription(),
                req.getCategory(),
                req.getBuyerPrice(),
                req.getStatus(),
                req.getCurrency(),
                req.getCreatedAt(),
                req.getUpdatedAt(),
                req.getImageUrls()
        );
    }

}

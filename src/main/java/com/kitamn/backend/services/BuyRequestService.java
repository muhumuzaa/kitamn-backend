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

    public BuyRequestResponse createBuy(CreateBuyRequest req, UserAccount buyer){

            BuyRequest newBuy = new BuyRequest();
            newBuy.setTitle(req.title());
            newBuy.setDescription(req.description());
            newBuy.setCategory(req.category());
            newBuy.setBuyerPrice(req.buyerPrice());
            newBuy.setStatus(ContentStatus.RequestStatus.OPEN);

            newBuy = buyRepo.save(newBuy);


        return toBuyResponse(newBuy);
    }

    //----------------- find all buy requests -----------------------------------
    @Transactional(readOnly =true)
    public Page<BuyRequestResponse> getAll(Pageable pageable){
        return buyRepo.findAll(pageable);
    }


    //----------------- find buy requests by Id -----------------------------------
    @Transactional(readOnly = true)
    public Optional<BuyRequestResponse> getById(Long id){
        var req = buyRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No buy requests with id: "+id));
        return Optional.ofNullable(toBuyResponse(req));
    }



    //----------------- update buy request -----------------------------------

    public BuyRequestResponse updateBuy(Long id, UpdateBuyRequest req){
        if(!buyRepo.existsById(id)){
            throw new IllegalArgumentException("The Request id: "+id +" you want to update does not exist." +id);
        }
        var toUpdate = new BuyRequest();

        toUpdate.setTitle(req.title()!= null? req.title(): toUpdate.getTitle());
        toUpdate.setDescription(req.description()!= null? req.description(): toUpdate.getDescription());
        toUpdate.setCategory(req.category()!= null? req.category(): toUpdate.getCategory());
        toUpdate.setBuyerPrice(req.buyerPrice()!= null? req.buyerPrice(): toUpdate.getBuyerPrice());

        toUpdate = buyRepo.save(toUpdate);
        return toBuyResponse(toUpdate);
    }

    public void delete(BuyRequest req){
        var buyReq = buyRepo.findById(req.getId()).orElseThrow(() -> new IllegalArgumentException("The buy req with id: "+req.getId()+" does not exist"));

        buyRepo.delete(req);
    }














    // ----------- Helpers ------------------------------

    public BuyRequestResponse toBuyResponse(BuyRequest req){
        return new BuyRequestResponse(
                req.getId(),
                req.getBuyer(),
                req.getTitle(),
                req.getDescription(),
                req.getCategory(),
                req.getBuyerPrice(),
                req.getStatus(),
                req.getCurrency(),
                req.getCreatedAt(),
                req.getUpdatedAt()
        );
    }

}

package com.kitamn.backend.services;

import com.kitamn.backend.domain.UserAccount;
import com.kitamn.backend.domain.UserAddress;
import com.kitamn.backend.dto.UserAddressDto.*;
import com.kitamn.backend.repos.UserAccountRepository;
import com.kitamn.backend.repos.UserAddressRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional()
public class UserAddressService {

    private UserAddressRepository addresses;
    private UserAccountRepository users;

    public UserAddressService(UserAddressRepository addresses, UserAccountRepository users){
        this.addresses = addresses;
        this.users =users;
    }

    public AddressResponse create(CreateOrAddressRequest req, Long userId){
        //check if user exists
        UserAccount ua = users.findById(userId).orElseThrow(()-> new IllegalArgumentException("User not found for id: "+userId));

        UserAddress addr = addresses.findByUser_Id(userId).orElseGet(() ->{
            var a = new UserAddress();
            a.setUser(ua);
            return a;
                }
        );

        addr.setLine1(req.line1());
        addr.setLine2(req.line2());
        addr.setCity(req.city());
        addr.setStateRegion(req.stateRegion());
        addr.setPostalCode(req.postalCode());
        addr.setCountry(req.country());
        addr.setLabel(req.label());


        var saved = addresses.save(addr);
        return toResponse(saved);
    }

    public AddressResponse getByUser(Long userId){
        var addr = addresses.findByUser_Id(userId).orElseThrow(() -> new IllegalArgumentException("No address found for user: "+userId));
        return toResponse(addr);
    }



    //response helper
    public AddressResponse toResponse(UserAddress a){
        return new AddressResponse(
                a.getId(),
                a.getLine1(),
                a.getLine2(),
                a.getCity(),
                a.getStateRegion(),
                a.getPostalCode(),
                a.getCountry(),
                a.getLabel(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}

package com.kitamn.backend.dto;

import com.kitamn.backend.domain.BuyRequest;
import com.kitamn.backend.domain.ProductCategories;
import com.kitamn.backend.domain.UserAccount;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ListingsDto {

    //-------BUY REQUEST ---Create ----------------------
    public record CreateBuyRequest(
            @NotBlank @Size(min=6, max=100) String title,
            @Size(max=400) String description,
            @NotNull ProductCategories category,
            @DecimalMin(value="0.0") BigDecimal buyerPrice,
            String currency
    ){}

    //-------BUY REQUEST ---Update ----------------------
    public record UpdateBuyRequest(
            @Size(min=6, max=100) String title,
            @Size(max=400) String description,
            ProductCategories category,
            @DecimalMin(value="0.0") BigDecimal buyerPrice
    ){}

    //-------BUY REQUEST ---Response ----------------------
    public record BuyRequestResponse(
            Long id,
            UserAccount buyer,
            String title,
            String description,
            ProductCategories category,
            BigDecimal buyerPrice,
            ContentStatus.RequestStatus status,
            String currency,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){}



    //------------------- SELLER ----------------------

    //-------Sell REQUEST ---Create ----------------------
    public record CreateSellerOffer(
            @DecimalMin(value="0.0") BigDecimal offerPrice,
            @Size(min=6, max=400) String message
    ){}

    //-------Sell REQUEST ---Update ----------------------
    public record UpdateSellerOffer(
            @DecimalMin(value="0.0") BigDecimal offerPrice,
            @Size(min=6, max=400) String message
    ){}

    //-------Sell REQUEST ---Response ----------------------
    public record SellerOfferResponse(
            Long id,
            UserAccount seller,
            BuyRequest buyRequest,
            BigDecimal offerPrice,
            String message,
            ContentStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){}
}

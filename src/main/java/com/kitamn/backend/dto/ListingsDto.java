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
import java.util.List;

public class ListingsDto {

    //-------BUY REQUEST ---Create ----------------------
    public record CreateBuyRequest(
            @NotBlank @Size(min=6, max=100) String title,
            @Size(max=400) String description,
            @NotNull ProductCategories category,
            @DecimalMin(value="0.0") BigDecimal buyerPrice,
            String currency,
            List<@Size(max=2048)String> imageUrls
    ){}

    //-------BUY REQUEST ---Update ----------------------
    public record UpdateBuyRequest(
            @Size(min=6, max=100) String title,
            @Size(max=400) String description,
            ProductCategories category,
            @DecimalMin(value="0.0") BigDecimal buyerPrice,
            List<@Size(max=2048)String> imageUrls
    ){}

    //-------BUY REQUEST ---Response ----------------------
    public record BuyRequestResponse(
            Long id,
            Long buyerId,
            String title,
            String description,
            ProductCategories category,
            BigDecimal buyerPrice,
            ContentStatus.RequestStatus status,
            String currency,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            List<String> imageUrls
    ){}



    //------------------- SELLER ----------------------

    //-------Sell REQUEST ---Create ----------------------
    public record CreateSellerOffer(
            Long sellerId, // TODO: to be replaced by principal when auth is implemented
            @Size(min=6, max=400) String message,
            List<@Size(max= 2048) String> imageUrls
    ){}

    //-------Sell REQUEST ---Update ----------------------
    public record UpdateSellerOffer(
            @Size(min=6, max=400) String message,
            List<@Size(max= 2048) String> imageUrls
    ){}

    //-------Sell REQUEST ---Response ----------------------
    public record SellerOfferResponse(
            Long id,
            Long requestId,
            Long sellerId,
            String message,
            ContentStatus.OfferStatus status,
            List<String> imageUrls,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ){}
}

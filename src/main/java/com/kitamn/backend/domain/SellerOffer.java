package com.kitamn.backend.domain;

import com.kitamn.backend.dto.ContentStatus;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="seller_offer")
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class SellerOffer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional =false)
    @JoinColumn(name ="seller_id", nullable=false)
    private UserAccount seller;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="buy_request_id", nullable=false)
    private BuyRequest buyRequest;


    @Column(nullable =false, length = 400)
    private String message;

    @ElementCollection
    @CollectionTable(name = "seller_offer_images", joinColumns = @JoinColumn(name = "seller_offer_id"))
    @Column(name = "url", length = 2048, nullable = false)
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable =false, length =16)
    private ContentStatus.OfferStatus status = ContentStatus.OfferStatus.ACTIVE;

    @Column(name="created_at", nullable =false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name="updated_at", nullable =false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}

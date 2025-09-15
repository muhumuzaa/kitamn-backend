package com.kitamn.backend.domain;

import com.kitamn.backend.dto.ContentStatus;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
    @JoinColumn(name="buy_request", nullable=false)
    private BuyRequest buyRequest;

    @Column(name="offer_price", nullable=false, precision=10, scale=1 )
    private BigDecimal offerPrice;

    @Column(nullable =false, length = 400)
    private String message;

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

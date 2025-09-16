package com.kitamn.backend.domain;

import com.kitamn.backend.dto.ContentStatus.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buy_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BuyRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="buyer_id", nullable =false)
    private UserAccount buyer;

    @Column(nullable =false, length = 100)
    private String title;

    @Column(length =400)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable =false, length =64)
    private ProductCategories category;

    @Column(name = "buyer_price", precision=10, scale=2)
    private BigDecimal buyerPrice;

    @Column(length = 3, nullable =false)
    private String currency ="CAD";

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length =16)
    private RequestStatus status= RequestStatus.OPEN;

//    private OffsetDateTime expiresAt;
    @ElementCollection
    @CollectionTable(name="req_images", joinColumns= @JoinColumn(name= "buy_request_id"))
    @Column(name="url", length =2048)
    private List<String> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy ="buyRequest", cascade=CascadeType.REMOVE, orphanRemoval =true)
    private List<SellerOffer> offers = new ArrayList<>();

    @CreationTimestamp
    private OffsetDateTime createdAt;
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

}

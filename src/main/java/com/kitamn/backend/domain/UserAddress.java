package com.kitamn.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Table(name="address", indexes={@Index(name="IX_address_user", columnList="user_id")})
@EntityListeners(AuditingEntityListener.class)
public class UserAddress {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max=100)
    @Column(name="line_1", nullable=false, length=100)
    private String line1;

    @Size(max=100)
    @Column(name="line_2", length=100)
    private String line2;

    @NotBlank @Size(max=100)
    @Column(nullable=false, length=100)
    private String city;

    @Size(max=100)
    @Column(name="state_region", length=100)
    private String stateRegion;

    @Size(max=100)
    @Column(name="postal_code", length=100)
    private String postalCode;

    @NotBlank @Size(max=100)
    @Column(nullable=false, length=100)
    private String country;

    @Size(max=50)
    @Column(length=50)
    private String label;

    @Column(name="is_primary", nullable=false)
    private boolean isPrimary =true;

    @NotBlank
    @Column(name="created_at", nullable=false, updatable=false)
    private OffsetDateTime createdAt;

    @NotBlank
    @Column(name="updated_at", nullable=false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private UserAccount user;

}

package com.kitamn.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_account")
@EntityListeners(AuditingEntityListener.class)
public class UserAccount{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Email @Size(max=100) @NaturalId
    @Column(nullable=false, unique=true, length=100)
    private String email;

    @NotBlank @Size(max=100)
    @Column(nullable = false, length=100)
    private String password;

    @NotBlank @Size(max=50)
    @Column(nullable=false, length=50)
    private String roles;

    @Column(name="is_enabled", nullable=false)
    private boolean enabled =false;

    @CreatedDate
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", nullable=false)
    private OffsetDateTime updatedAt;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL,  orphanRemoval=true, fetch=FetchType.LAZY)
    private UserProfile profile;



}
package com.kitamn.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="user_profile", uniqueConstraints = @UniqueConstraint(name = "UQ_user_profile_user", columnNames = "user_id"))
@EntityListeners(AuditingEntityListener.class)
public class UserProfile {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max=100)
    @Column(name="first_name", nullable=false, length =100)
    private String firstName;

    @NotBlank @Size(max=100)
    @Column(name="last_name", nullable=false, length =100)
    private String lastName;

    @Size(max=30)
    @Column( length =30)
    private String phone;

    @Size(max=500)
    @Column(name="avatar_url", length =500)
    private String avatarUrl;

    @Size(max=500)
    @Column(length=500)
    private String bio;

//    @NotBlank @Size(max=100)
//    @Column(name="line1", nullable=false, length =100)
//    private String line1;

    @CreatedDate
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", nullable=false)
    private OffsetDateTime updatedAt;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false, unique=true)
    private UserAccount user;
}

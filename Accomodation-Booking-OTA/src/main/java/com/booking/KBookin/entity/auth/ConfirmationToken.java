package com.booking.KBookin.entity.auth;
import java.time.Instant;
import java.time.LocalDateTime;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
@Getter
@Setter
@Builder
public class ConfirmationToken extends BaseEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private long token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
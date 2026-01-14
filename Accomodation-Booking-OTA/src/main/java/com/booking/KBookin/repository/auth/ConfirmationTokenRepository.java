package com.booking.KBookin.repository.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.KBookin.entity.auth.ConfirmationToken;

import jakarta.transaction.Transactional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long>, JpaSpecificationExecutor<ConfirmationToken> {
    @Query("SELECT c FROM ConfirmationToken c WHERE c.token = :token AND c.user.email = :email")
    Optional<ConfirmationToken> findByTokenAndUserEmail(@Param("token") long token, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE confirmation_token
        SET confirmed_at = :confirmedAt
        WHERE token = :token
          AND user_id = (
              SELECT id FROM users WHERE email = :email
          )
        """,
            nativeQuery = true
    )
    void updateConfirmedAt(
            @Param("token") long token,
            @Param("confirmedAt") LocalDateTime confirmedAt,
            @Param("email") String email
    );
}

package com.booking.KBookin.repository.booking;

import com.booking.KBookin.entity.booking.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<CheckIn,Long> {
    CheckIn findByBookingId(Long bookingId);

    @Query("""
        select distinct ci
        from CheckIn ci
        join fetch ci.rooms r
        where ci.booking.id = :bookingId
    """)
    Optional<CheckIn> findByBookingIdForCheckout(@Param("bookingId") Long bookingId);

}

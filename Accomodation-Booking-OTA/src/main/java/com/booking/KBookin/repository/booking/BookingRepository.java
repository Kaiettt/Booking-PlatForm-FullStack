package com.booking.KBookin.repository.booking;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.repository.projection.booking.BookingProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("""
        select distinct b
        from Booking b
        join fetch b.bookingItems bi
        join fetch bi.roomType rt
        where b.bookingReference = :txnRef
    """)
    Booking findByBookingReference(String txnRef);

    @Query("""
        select distinct b
        from Booking b
        join fetch b.bookingItems bi
        join fetch bi.roomType rt
        join fetch bi.ratePlan rp
        join fetch rp.cancellationPolicy
        where b.id = :bookingId
    """)
    Optional<Booking> findByIdForCancellation(Long bookingId);


    @EntityGraph(attributePaths = {
            "bookingItems",
            "bookingItems.roomType",
            "bookingItems.ratePlan",
            "bookingItems.ratePlan.perks"
    })
    Optional<BookingProjection> findProjectedById(Long id);
}

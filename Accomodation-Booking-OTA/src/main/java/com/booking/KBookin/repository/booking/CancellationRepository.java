package com.booking.KBookin.repository.booking;

import com.booking.KBookin.entity.booking.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationRepository extends JpaRepository<Cancellation,Long> {
}

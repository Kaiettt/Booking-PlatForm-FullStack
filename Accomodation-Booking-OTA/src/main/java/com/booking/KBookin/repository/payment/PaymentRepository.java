package com.booking.KBookin.repository.payment;

import com.booking.KBookin.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}

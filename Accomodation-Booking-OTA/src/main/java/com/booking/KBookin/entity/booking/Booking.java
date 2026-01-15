// src/main/java/com/booking/KBookin/entity/booking/Booking.java
package com.booking.KBookin.entity.booking;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.payment.Payment;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.booking.BookingStatus;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.enumerate.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;


    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Embedded
    private BookingGuest guest;

    @Column(name = "special_request", columnDefinition = "TEXT")
    private String specialRequest;

    @Column(name = "booking_reference")
    private String bookingReference;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @Builder.Default
    private List<BookingItem> bookingItems = new ArrayList<>();


    public void handleBookingPaymentMethod() {
        if(this.paymentMethod == PaymentMethod.CASH){
            this.status = BookingStatus.COMPLETED;
        }
        else if(this.paymentMethod == PaymentMethod.VN_PAY){
            this.status = BookingStatus.PENDING_PAYMENT;
        }
        this.generateBookingReference();
    }

    public void calculateTotalAmount() {
        this.totalAmount = bookingItems.stream()
                .map(BookingItem::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void generateBookingReference() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String uid = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        this.bookingReference = "BOOKING-" + timestamp + "-" + uid;
    }

    public void handlePaymentSuccess() {
        this.paymentStatus = PaymentStatus.PAID;
        this.status = BookingStatus.COMPLETED;
    }

    public void handlePaymentFailed() {

        this.paymentStatus = PaymentStatus.FAILED;
        this.status = BookingStatus.PENDING;
    }
}
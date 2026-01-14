package com.booking.KBookin.dto.booking;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookingResponse implements Serializable {

    private Long id;
    private Long userId;

    private String status;
    private String paymentMethod;
    private String paymentStatus;

    private BigDecimal totalAmount;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private BookingGuestResponse guest;

    private String specialRequest;

    private String bookingReference;
    private List<BookingItemResponse> bookingItems;

}

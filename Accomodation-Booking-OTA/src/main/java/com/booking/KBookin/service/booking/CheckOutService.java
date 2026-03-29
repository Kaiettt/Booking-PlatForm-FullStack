package com.booking.KBookin.service.booking;

import com.booking.KBookin.dto.checkin.CheckoutResponse;

public interface CheckOutService {
    CheckoutResponse handleCheckout(Long bookingId);
}

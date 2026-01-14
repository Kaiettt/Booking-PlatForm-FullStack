package com.booking.KBookin.service.booking;

import com.booking.KBookin.dto.checkin.CheckoutResponse;
import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.checkin.CheckinResponse;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequestDTO request);

    CheckinResponse handleCheckin(Long bookingId);

    CheckoutResponse handleCheckout(Long bookingId);

    BookingResponse getBookingById(Long id);
}

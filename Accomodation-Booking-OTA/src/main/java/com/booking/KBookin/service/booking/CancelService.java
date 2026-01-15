package com.booking.KBookin.service.booking;

import com.booking.KBookin.dto.booking.CancelBookingRequestDTO;
import com.booking.KBookin.dto.booking.CancellationResponse;
import jakarta.validation.Valid;

public interface CancelService {
    CancellationResponse cancelBooking(CancelBookingRequestDTO request);
}

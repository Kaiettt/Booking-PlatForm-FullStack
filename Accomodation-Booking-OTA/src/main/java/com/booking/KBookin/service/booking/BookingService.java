package com.booking.KBookin.service.booking;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.dto.booking.BookingResponse;
import java.util.List;

public interface BookingService extends CheckInService, CheckOutService {
    BookingResponse createBooking(CreateBookingRequestDTO request);
    BookingResponse getBookingById(Long id);

    List<BookingResponse> getBookingsByHostId(Long hostId);
}

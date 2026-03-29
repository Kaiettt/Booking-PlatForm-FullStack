package com.booking.KBookin.service.booking.validation;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;

public interface BookingValidator {
    void validate(CreateBookingRequestDTO request);
    void setNext(BookingValidator next);
}

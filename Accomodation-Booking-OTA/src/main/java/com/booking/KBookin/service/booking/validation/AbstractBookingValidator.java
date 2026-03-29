package com.booking.KBookin.service.booking.validation;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;

public abstract class AbstractBookingValidator implements BookingValidator {
    private BookingValidator next;

    @Override
    public void setNext(BookingValidator next) {
        this.next = next;
    }

    protected void next(CreateBookingRequestDTO request) {
        if (next != null) {
            next.validate(request);
        }
    }
}

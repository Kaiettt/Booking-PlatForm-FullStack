package com.booking.KBookin.service.booking.validation;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.exception.BusinessProcessException;
import org.springframework.stereotype.Component;

@Component
public class BookingItemsValidator extends AbstractBookingValidator {
    @Override
    public void validate(CreateBookingRequestDTO request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessProcessException("Booking must contain at least one item");
        }
        next(request);
    }
}

package com.booking.KBookin.service.booking.validation;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.exception.BusinessProcessException;
import org.springframework.stereotype.Component;

@Component
public class DateRangeValidator extends AbstractBookingValidator {
    @Override
    public void validate(CreateBookingRequestDTO request) {
        if (request.getCheckIn().isAfter(request.getCheckOut())) {
            throw new BusinessProcessException("Check-in date must be before check-out date");
        }
        next(request);
    }
}

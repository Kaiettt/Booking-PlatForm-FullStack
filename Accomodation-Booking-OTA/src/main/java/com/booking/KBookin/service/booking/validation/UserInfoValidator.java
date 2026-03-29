package com.booking.KBookin.service.booking.validation;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.exception.BusinessProcessException;
import org.springframework.stereotype.Component;

@Component
public class UserInfoValidator extends AbstractBookingValidator {
    @Override
    public void validate(CreateBookingRequestDTO request) {
        if (request.getUserId() == null) {
            throw new BusinessProcessException("User ID must not be null");
        }
        if (request.getGuest() == null || request.getGuest().getEmail() == null) {
            throw new BusinessProcessException("Guest contact information is required");
        }
        next(request);
    }
}

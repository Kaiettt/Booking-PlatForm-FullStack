package com.booking.KBookin.service.booking.validation;

import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingValidationChain {
    private final List<BookingValidator> validators;
    private BookingValidator first;

    @PostConstruct
    public void init() {
        if (validators.isEmpty()) return;
        
        for (int i = 0; i < validators.size() - 1; i++) {
            validators.get(i).setNext(validators.get(i + 1));
        }
        first = validators.get(0);
    }

    public void validate(CreateBookingRequestDTO request) {
        if (first != null) {
            first.validate(request);
        }
    }
}

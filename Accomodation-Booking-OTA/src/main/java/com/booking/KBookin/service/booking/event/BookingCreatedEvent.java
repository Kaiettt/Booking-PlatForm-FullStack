package com.booking.KBookin.service.booking.event;

import com.booking.KBookin.dto.booking.BookingResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookingCreatedEvent extends ApplicationEvent {
    private final BookingResponse bookingResponse;

    public BookingCreatedEvent(Object source, BookingResponse bookingResponse) {
        super(source);
        this.bookingResponse = bookingResponse;
    }
}

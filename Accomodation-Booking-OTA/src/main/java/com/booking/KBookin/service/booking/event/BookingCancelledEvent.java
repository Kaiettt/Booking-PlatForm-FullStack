package com.booking.KBookin.service.booking.event;

import com.booking.KBookin.dto.booking.CancellationResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookingCancelledEvent extends ApplicationEvent {
    private final CancellationResponse cancellationResponse;

    public BookingCancelledEvent(Object source, CancellationResponse cancellationResponse) {
        super(source);
        this.cancellationResponse = cancellationResponse;
    }
}

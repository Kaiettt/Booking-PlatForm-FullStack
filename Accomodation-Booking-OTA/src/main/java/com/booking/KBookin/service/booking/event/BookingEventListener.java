package com.booking.KBookin.service.booking.event;

import com.booking.KBookin.kafka.producer.email.EmailBookingSuccessProducer;
import com.booking.KBookin.kafka.producer.email.EmailCancelBookingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventListener {
    private final EmailBookingSuccessProducer successEmailProducer;
    private final EmailCancelBookingProducer cancelEmailProducer;

    @Async
    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        successEmailProducer.sendBookingSuccessEvent(event.getBookingResponse());
    }

    @Async
    @EventListener
    public void handleBookingCancelled(BookingCancelledEvent event) {
        cancelEmailProducer.sendCancelBooking(event.getCancellationResponse());
    }
}

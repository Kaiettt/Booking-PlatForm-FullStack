package com.booking.KBookin.service.booking.strategy.action;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnlineBookingActionStrategy implements BookingActionStrategy {
    private final RoomService roomService;

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.VN_PAY; // Also covers others if logic is same, or use a list
    }

    @Override
    public void process(Booking booking) {
        // Extend room locks for online bookings until callback
        roomService.extendRoomLock(booking.getUser().getId());
    }
}

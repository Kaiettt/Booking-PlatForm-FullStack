package com.booking.KBookin.service.booking.strategy.action;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashBookingActionStrategy implements BookingActionStrategy {
    private final RoomService roomService;

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.CASH;
    }

    @Override
    public void process(Booking booking) {
        // Release room locks immediately for CASH bookings
        roomService.releaseAllRoomLock(booking.getUser().getId());
    }
}

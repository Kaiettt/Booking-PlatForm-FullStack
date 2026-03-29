package com.booking.KBookin.service.booking.strategy.action;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.enumerate.payment.PaymentMethod;

public interface BookingActionStrategy {
    PaymentMethod getMethod();
    void process(Booking booking);
}

package com.booking.KBookin.service.booking.strategy.action;

import com.booking.KBookin.enumerate.payment.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BookingActionStrategyFactory {
    private final Map<PaymentMethod, BookingActionStrategy> strategies;

    public BookingActionStrategyFactory(List<BookingActionStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(BookingActionStrategy::getMethod, s -> s));
    }

    public BookingActionStrategy getStrategy(PaymentMethod method) {
        BookingActionStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("No booking action strategy found for: " + method);
        }
        return strategy;
    }
}

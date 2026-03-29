package com.booking.KBookin.service.booking.strategy.cancellation;

import com.booking.KBookin.enumerate.booking.CancellationPolicyType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CancellationStrategyFactory {
    private final Map<CancellationPolicyType, CancellationStrategy> strategies;

    public CancellationStrategyFactory(List<CancellationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(CancellationStrategy::getType, s -> s));
    }

    public CancellationStrategy getStrategy(CancellationPolicyType type) {
        CancellationStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for policy type: " + type);
        }
        return strategy;
    }
}

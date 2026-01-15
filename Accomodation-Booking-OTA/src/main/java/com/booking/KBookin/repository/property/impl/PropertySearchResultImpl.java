package com.booking.KBookin.repository.property.impl;

import com.booking.KBookin.repository.property.PropertySearchResult;

import java.math.BigDecimal;

public class PropertySearchResultImpl implements PropertySearchResult {

    private final Long id;
    private final BigDecimal minPrice;

    // Constructor cần cho @ConstructorResult
    public PropertySearchResultImpl(Long id, BigDecimal minPrice) {
        this.id = id;
        this.minPrice = minPrice;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public BigDecimal getMinPrice() {
        return minPrice;
    }
}

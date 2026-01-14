package com.booking.KBookin.repository.property;

import com.booking.KBookin.dto.search.FilterSearchRequest;
import com.booking.KBookin.dto.search.SearchDetailPropertyDTO;
import com.booking.KBookin.entity.property.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertySearchCustomRepository {
    Page<PropertySearchResult> searchPropertyWithFilter(FilterSearchRequest request, Pageable pageable);

    Property searchRoomsWithFilter(SearchDetailPropertyDTO request);
}

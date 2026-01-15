package com.booking.KBookin.service.search;

import com.booking.KBookin.dto.PageResponse;
import com.booking.KBookin.dto.property.PropertyDetailResponseDTO;
import com.booking.KBookin.dto.search.FilterSearchRequest;
import com.booking.KBookin.dto.search.SearchDetailPropertyDTO;
import com.booking.KBookin.dto.search.SearchRequestDTO;
import com.booking.KBookin.dto.search.SearchResponseDTO;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Pageable;
public interface SearchService {
    PageResponse<List<SearchResponseDTO>> searchProperty(SearchRequestDTO request, Pageable pageable);

    PageResponse<List<SearchResponseDTO>> searchPropertyWithFilter(FilterSearchRequest request,Pageable pageable);

    PropertyDetailResponseDTO searchRoomTypeWithFilter(SearchDetailPropertyDTO request);
}

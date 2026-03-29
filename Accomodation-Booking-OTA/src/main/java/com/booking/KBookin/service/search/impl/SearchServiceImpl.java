package com.booking.KBookin.service.search.impl;

import com.booking.KBookin.dto.PageResponse;
import com.booking.KBookin.dto.property.PropertyDetailResponseDTO;
import com.booking.KBookin.dto.search.FilterSearchRequest;
import com.booking.KBookin.dto.search.SearchDetailPropertyDTO;
import com.booking.KBookin.mapper.property.PropertyMapper;
import com.booking.KBookin.repository.property.PropertySearchResult;
import com.booking.KBookin.dto.search.SearchRequestDTO;
import com.booking.KBookin.dto.search.SearchResponseDTO;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.repository.property.impl.PropertySearchCustomFilterRepository;
import com.booking.KBookin.service.search.SearchResultMapper;
import com.booking.KBookin.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private final PropertyRepository propertyRepository;
    private final PropertySearchCustomFilterRepository propertySearchCustomFilterRepository;
    private final PropertyMapper propertyMapper;
    private final SearchResultMapper searchResultMapper;
    @Override
    @Cacheable(
            value = "search-properties",
            key = "#request.city + '_' + #request.adults + '_' + #request.children + '_' + #pageable.pageNumber + '_' + #pageable.pageSize",
            unless = "#result == null"
    )
    public PageResponse<List<SearchResponseDTO>> searchProperty(SearchRequestDTO request, Pageable pageable) {
        Page<PropertySearchResult> priceResults = propertyRepository.findPropertyWithMinPrice(
                request.city(),
                request.adults(),
                request.children(),
                pageable
        );
        return processSearchResults(priceResults);
    }

    @Cacheable(
            value = "search-properties",
            key = "T(java.util.Objects).hash(#request, #pageable.pageNumber, #pageable.pageSize)"
    )
    public PageResponse<List<SearchResponseDTO>> searchPropertyWithFilter(FilterSearchRequest request,Pageable pageable) {
        Page<PropertySearchResult> resultPage =
                this.propertySearchCustomFilterRepository.searchPropertyWithFilter(request, pageable);

        return processSearchResults(resultPage);
    }
    @Override
    @Cacheable(
            value = "search-room-types",
            key = "T(java.util.Objects).hash(#request)"
    )
    public PropertyDetailResponseDTO searchRoomTypeWithFilter(SearchDetailPropertyDTO request) {
        Property property = this.propertySearchCustomFilterRepository.searchRoomsWithFilter(request);

        // --- PROPERTY-LEVEL MAPPINGS ---

        return propertyMapper.toDetailDto(property);
    }

    private PageResponse<List<SearchResponseDTO>> processSearchResults(Page<PropertySearchResult> resultPage) {
        Map<Long, BigDecimal> priceMap = searchResultMapper.extractPriceMap(resultPage);
        List<Property> properties = propertyRepository.findAllById(priceMap.keySet());
        List<SearchResponseDTO> responseDTO = searchResultMapper.mapToResponseDTO(properties, priceMap);

        return PageResponse.<List<SearchResponseDTO>>builder()
                .currentPage(resultPage.getNumber())
                .totalPages(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .data(responseDTO)
                .build();
    }

}
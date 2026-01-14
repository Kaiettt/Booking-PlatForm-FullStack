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
import com.booking.KBookin.repository.property.impl.PropertySearchCustomRepositoryImpl;
import com.booking.KBookin.service.search.SearchService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private PropertyRepository propertyRepository;
    private PropertySearchCustomRepositoryImpl propertySearchCustomRepositoryImpl;
    private PropertyMapper propertyMapper;
    @Override
    @Cacheable(
            value = "search-properties",
            key = "#request.city + '_' + #request.adults + '_' + #request.children + '_' + #pageable.pageNumber + '_' + #pageable.pageSize",
            unless = "#result == null"
    )
    public PageResponse<List<SearchResponseDTO>> searchProperty(SearchRequestDTO request, Pageable pageable) {
        // Get property IDs with min prices
        Page<PropertySearchResult> priceResults = propertyRepository.findPropertyWithMinPrice(
                request.city(),
                request.adults(),
                request.children(),
                pageable
        );


        Map<Long, BigDecimal> priceMap = priceResults.getContent().stream()
                .collect(Collectors.toMap(
                        PropertySearchResult::getId,
                        PropertySearchResult::getMinPrice
                ));


        // Lấy ID theo đúng thứ tự để tránh sai thứ tự trong kết quả trả về
        List<Long> propertyIds = priceResults.getContent().stream()
                .map(PropertySearchResult::getId)
                .toList();

        // Lấy properties đầy đủ
        List<Property> properties = propertyRepository.findAllById(propertyIds);

        // Map sang response DTO
        List<SearchResponseDTO> responseDTO = toResponseDTO(properties,priceMap);

        return PageResponse.<List<SearchResponseDTO>>builder()
                .currentPage(priceResults.getNumber())
                .totalPages(priceResults.getTotalPages())
                .totalElements(priceResults.getTotalElements())
                .data(responseDTO)
                .build();
    }

    @Cacheable(
            value = "search-properties",
            key = "T(java.util.Objects).hash(#request, #pageable.pageNumber, #pageable.pageSize)"
    )
    public PageResponse<List<SearchResponseDTO>> searchPropertyWithFilter(FilterSearchRequest request,Pageable pageable) {
        Page<PropertySearchResult> resultPage =
                this.propertySearchCustomRepositoryImpl.searchPropertyWithFilter(request, pageable);

        Map<Long,BigDecimal> priceMap  = resultPage.getContent().stream()
                .collect(Collectors.toMap(
                        PropertySearchResult::getId,
                        PropertySearchResult::getMinPrice
                ));

        List<Property> properties = this.propertyRepository.findAllById(priceMap.keySet());


        List<SearchResponseDTO> responseDTO = toResponseDTO(properties,priceMap);

        return PageResponse.<List<SearchResponseDTO>>builder()
                .currentPage(resultPage.getNumber())
                .totalPages(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .data(responseDTO)
                .build();
    }
    @Override
    @Cacheable(
            value = "search-room-types",
            key = "T(java.util.Objects).hash(#request)"
    )
    public PropertyDetailResponseDTO searchRoomTypeWithFilter(SearchDetailPropertyDTO request) {
        Property property = this.propertySearchCustomRepositoryImpl.searchRoomsWithFilter(request);

        // --- PROPERTY-LEVEL MAPPINGS ---

        return propertyMapper.toDetailDto(property);
    }

    public List<SearchResponseDTO> toResponseDTO(List<Property> properties,Map<Long, BigDecimal> priceMap){
        return properties.stream()
                .map(property -> {

                    // Lấy DTO cơ bản từ MapStruct
                    SearchResponseDTO dto = propertyMapper.toSearchDto(property);

                    // Áp dụng giá tối thiểu (đây là logic bên ngoài Entity, phải làm thủ công)
                    dto.setMinPrice(priceMap.get(property.getId()));
                    return dto;
                })
                .toList();

    }

}
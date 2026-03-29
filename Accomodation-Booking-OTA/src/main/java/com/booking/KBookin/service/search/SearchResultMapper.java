package com.booking.KBookin.service.search;

import com.booking.KBookin.dto.search.SearchResponseDTO;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.mapper.property.PropertyMapper;
import com.booking.KBookin.repository.property.PropertySearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchResultMapper {
    private final PropertyMapper propertyMapper;

    public List<SearchResponseDTO> mapToResponseDTO(List<Property> properties, Map<Long, BigDecimal> priceMap) {
        return properties.stream()
                .map(property -> {
                    SearchResponseDTO dto = propertyMapper.toSearchDto(property);
                    dto.setMinPrice(priceMap.get(property.getId()));
                    return dto;
                })
                .toList();
    }

    public Map<Long, BigDecimal> extractPriceMap(Page<PropertySearchResult> resultPage) {
        return resultPage.getContent().stream()
                .collect(Collectors.toMap(
                        PropertySearchResult::getId,
                        PropertySearchResult::getMinPrice
                ));
    }
}

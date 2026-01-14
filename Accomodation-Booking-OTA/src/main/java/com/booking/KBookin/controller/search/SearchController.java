package com.booking.KBookin.controller.search;

import com.booking.KBookin.dto.PageResponse;
import com.booking.KBookin.dto.property.PropertyDetailResponseDTO;
import com.booking.KBookin.dto.search.FilterSearchRequest;
import com.booking.KBookin.dto.search.SearchDetailPropertyDTO;
import com.booking.KBookin.dto.search.SearchRequestDTO;
import com.booking.KBookin.dto.search.SearchResponseDTO;
import com.booking.KBookin.service.search.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
@RestController
@AllArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private SearchService searchService;
    @GetMapping
    public ResponseEntity<PageResponse<List<SearchResponseDTO>>> searchProperty(
            @RequestParam String city,
            @RequestParam LocalDate checkingDate,
            @RequestParam LocalDate checkoutDate,
            @RequestParam int adults,
            @RequestParam int children,
            Pageable pageable
    ){
        SearchRequestDTO request = new SearchRequestDTO(city, checkingDate, checkoutDate, adults, children);
        return ResponseEntity.ok(this.searchService.searchProperty(request, pageable));
    }
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<List<SearchResponseDTO>>> filterSearch(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate checkingDate,
            @RequestParam(required = false) LocalDate checkoutDate,
            @RequestParam(required = false) Integer adults,
            @RequestParam(required = false) Integer children,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<String> propertyTypes,          // hotel, apartment, villa...
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) List<String> propertyAmenities, // optional list
            @RequestParam(required = false) List<String> propertyFacilities,
            @RequestParam(required = false) List<String> roomFacilities,
            Pageable pageable
    ){
        FilterSearchRequest request = new FilterSearchRequest(
                city, checkingDate, checkoutDate, adults, children,
                minPrice, maxPrice, propertyTypes, minRating,
                propertyAmenities, propertyFacilities,roomFacilities
        );
        return ResponseEntity.ok(this.searchService.searchPropertyWithFilter(request,pageable));
    }
    @GetMapping("/room-type/filter")
    public ResponseEntity<PropertyDetailResponseDTO> filterRoomSearch(

            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) LocalDate checkingDate,
            @RequestParam(required = false) LocalDate checkoutDate,
            @RequestParam(required = false) Integer adults,
            @RequestParam(required = false) Integer children,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) List<String> roomFacilities
    ) {
        SearchDetailPropertyDTO request = new SearchDetailPropertyDTO(
                propertyId,
                checkingDate,
                checkoutDate,
                adults,
                children,
                minPrice,
                maxPrice,
                minRating,
                roomType,
                roomFacilities
        );
        return ResponseEntity.ok(
                searchService.searchRoomTypeWithFilter(request)
        );
    }


}

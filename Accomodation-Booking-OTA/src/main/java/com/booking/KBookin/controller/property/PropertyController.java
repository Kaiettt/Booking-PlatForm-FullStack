package com.booking.KBookin.controller.property;

import com.booking.KBookin.dto.PageResponse;
import com.booking.KBookin.dto.document.PropertyDocumentCreateRequest;
import com.booking.KBookin.dto.property.PropertyCreateItemRequest;
import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.dto.room.RoomTypeFilterDTO;
import com.booking.KBookin.repository.projection.property.PropertyHostProjection;
import com.booking.KBookin.repository.projection.review.ReviewProjection;
import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;
import com.booking.KBookin.service.document.DocumentService;
import com.booking.KBookin.service.property.PropertyService;
import com.booking.KBookin.service.review.ReviewService;
import com.booking.KBookin.service.room.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyService propertyService;
    private final DocumentService documentService;
    private final RoomService roomService;
    private final ReviewService reviewService;
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PropertyHostProjection> findPropertyById(@PathVariable long id){
        return ResponseEntity.ok(this.propertyService.fetchPropertyIdById(id));
    }
    @PreAuthorize("hasRole('HOST')")
    @PostMapping
    public ResponseEntity<Long> createProperty(
            @Valid @RequestBody PropertyCreateRequest request) {

        Long propertyId = propertyService.createProperty(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(propertyId)
                .toUri();

        return ResponseEntity.created(location).body(propertyId);
    }
    @PreAuthorize("hasRole('HOST')")
    @PutMapping(value = "/{propertyId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updatePropertyDocument(
            @PathVariable Long propertyId, // Capture the ID from the URL
            @Valid @ModelAttribute PropertyDocumentCreateRequest request) { // Use ModelAttribute for files

        Long updatedPropertyId = documentService.updatePropertyDocument(propertyId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.ok(updatedPropertyId);
    }
    @PreAuthorize("hasRole('HOST')")
    @PutMapping(value = "/{propertyId}/items", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updatePropertyItems(
            @PathVariable Long propertyId, // Capture the ID from the URL
            @Valid @ModelAttribute PropertyCreateItemRequest request) { // Use ModelAttribute for files

        Long updatedPropertyId = propertyService.updatePropertyItems(propertyId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.ok(updatedPropertyId);
    }
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @GetMapping("/room-type/{propertyId}")
    public ResponseEntity<List<RoomTypeHostProjection>> findPropertyRoomsById(@PathVariable long propertyId, @Valid RoomTypeFilterDTO filter){
        return ResponseEntity.ok(this.roomService.fetchPropertyRoomsById(propertyId,filter));
    }

    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @GetMapping("/reviews/{propertyId}")
    public ResponseEntity<PageResponse<List<ReviewProjection>>> findPropertyReviewsById(@PathVariable long propertyId, Pageable pageable){
        return ResponseEntity.ok(this.reviewService.fetchPropertyReviewsById(propertyId,pageable));
    }
}

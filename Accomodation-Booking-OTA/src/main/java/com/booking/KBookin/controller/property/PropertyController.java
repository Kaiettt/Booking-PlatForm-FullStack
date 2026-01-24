package com.booking.KBookin.controller.property;

import com.booking.KBookin.dto.document.PropertyDocumentCreateRequest;
import com.booking.KBookin.dto.property.PropertyCreateItemRequest;
import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.service.document.DocumentService;
import com.booking.KBookin.service.property.PropertyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@AllArgsConstructor
@RestController
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyService propertyService;
    private final DocumentService documentService;
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
}

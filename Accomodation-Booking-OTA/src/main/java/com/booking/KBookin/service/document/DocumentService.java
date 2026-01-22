package com.booking.KBookin.service.document;

import com.booking.KBookin.dto.document.PropertyDocumentCreateRequest;
import jakarta.validation.Valid;

public interface DocumentService {
    Long updatePropertyDocument(Long propertyId,PropertyDocumentCreateRequest request);
}

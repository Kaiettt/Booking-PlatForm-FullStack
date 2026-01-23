package com.booking.KBookin.service.document;

import com.booking.KBookin.dto.document.PropertyDocumentCreateRequest;
import com.booking.KBookin.entity.document.ComplianceDocument;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.repository.document.ComplianceDocumentRepository;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.service.resource.ResourceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.booking.KBookin.config.MinioBucketConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ComplianceServiceImpl implements DocumentService{
    private final ResourceService resourceService;
    private final ComplianceDocumentRepository complianceDocumentRepository;
    private final PropertyRepository propertyRepository;
    private final MinioBucketConfig minioBucketConfig;
    @Override
    public Long updatePropertyDocument(Long propertyId, PropertyDocumentCreateRequest request) {
        Property property = this.propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property Not Found"));
        List<ComplianceDocument> complianceDocumentList = new ArrayList<>();
        request.getPropertyDetailDocuments().forEach(docDto ->{
            String fileExtension = getFileExtension(docDto.getFile().getOriginalFilename());
            String objectKey = String.format("compliance/%d/%s_%s%s",
                    propertyId,
                    docDto.getType(),
                    UUID.randomUUID().toString().substring(0, 8),
                    fileExtension);
            resourceService.uploadFile(minioBucketConfig.getCompliance(), objectKey, docDto.getFile());
            complianceDocumentList.add(ComplianceDocument.builder()
                    .property(property)
                    .type(docDto.getType())
                    .objectUrl(objectKey)
                    .contentType(docDto.getFile().getContentType())
                    .fileSize(docDto.getFile().getSize())
                    .build());
        });
        this.complianceDocumentRepository.saveAll(complianceDocumentList);
        return propertyId;
    }
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf("."));
    }

}

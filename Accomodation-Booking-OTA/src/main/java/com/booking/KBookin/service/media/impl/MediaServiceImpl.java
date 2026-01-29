package com.booking.KBookin.service.media.impl;

import com.booking.KBookin.config.MinioBucketConfig;
import com.booking.KBookin.entity.media.Media;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.enumerate.property.DocumentType;
import com.booking.KBookin.service.media.MediaService;
import com.booking.KBookin.service.resource.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.booking.KBookin.util.FileUtil.getFileExtension;

@AllArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {
    private final ResourceService resourceService;;
    private final MinioBucketConfig minioBucketConfig;

    @Override
    public void updatePropertyImages(List<MultipartFile> images, Property property) {
        for (MultipartFile file : images) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String objectKey = String.format("property-images/%d/%s_%s%s",
                    property.getId(),
                    DocumentType.PROPERTY_DEED,
                    UUID.randomUUID().toString().substring(0, 8),
                    fileExtension);
            resourceService.uploadFile(minioBucketConfig.getGallery(), objectKey,file);
            Media media = Media.builder()
                    .url(objectKey)
                    .property(property)
                    .build();
            property.getMedia().add(media);
        }
    }
}

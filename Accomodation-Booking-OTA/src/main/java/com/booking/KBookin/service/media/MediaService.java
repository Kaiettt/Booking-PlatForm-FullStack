package com.booking.KBookin.service.media;

import com.booking.KBookin.entity.property.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    void updatePropertyImages(List<MultipartFile> images, Property property);
}

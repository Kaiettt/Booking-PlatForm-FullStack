package com.booking.KBookin.service.resource;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    String uploadFile(String bucket, String objectKey, MultipartFile file);

    String getTemporaryUrl(String bucket, String objectKey, int durationSeconds);

    void removeFile(String bucket, String objectKey);

    boolean bucketExists(String bucket);
}
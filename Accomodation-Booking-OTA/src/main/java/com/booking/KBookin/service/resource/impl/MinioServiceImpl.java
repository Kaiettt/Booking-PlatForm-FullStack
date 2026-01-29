package com.booking.KBookin.service.resource.impl;

import com.booking.KBookin.service.resource.ResourceService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements ResourceService {

    private final MinioClient minioClient;

    @Override
    public String uploadFile(String bucket, String objectKey, MultipartFile file) {
        try {
            ensureBucketExists(bucket);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectKey;
        } catch (Exception e) {
            log.error("Failed to upload file to bucket: {}", bucket, e);
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    @Override
    public String getTemporaryUrl(String bucket, String objectKey, int durationSeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(durationSeconds)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for key: {}", objectKey, e);
            throw new RuntimeException("Link generation failed");
        }
    }

    @Override
    public void removeFile(String bucket, String objectKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build()
            );
        } catch (Exception e) {
            log.error("Failed to remove file: {}", objectKey, e);
        }
    }

    @Override
    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureBucketExists(String bucket) throws Exception {
        if (!bucketExists(bucket)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("Created new bucket: {}", bucket);
        }
    }
}
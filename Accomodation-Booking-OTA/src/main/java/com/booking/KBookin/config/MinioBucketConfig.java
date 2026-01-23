package com.booking.KBookin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio.bucket")
public class MinioBucketConfig {
    private String compliance;
    private String gallery;
}
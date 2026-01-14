package com.booking.KBookin.config;

import com.booking.KBookin.util.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            return SecurityUtil.getCurrentUserLogin()
                    .or(() -> Optional.of("System"));
        };
    }
}

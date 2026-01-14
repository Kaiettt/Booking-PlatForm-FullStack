package com.booking.KBookin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    
    @CreatedDate
    @Column(name = "created_at",nullable = true)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = true)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by",nullable = true)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by",nullable = true)
    private String updatedBy;
    
    @Column(name = "is_deleted",nullable = true)
    private Boolean isDeleted;
}
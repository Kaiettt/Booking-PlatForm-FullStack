package com.booking.KBookin.entity.document;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.enumerate.property.DocumentType;
import com.booking.KBookin.entity.property.Property;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compliance_documents")
public class ComplianceDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Column(nullable = false)
    private String objectUrl;

    @Column(nullable = false)
    private String contentType;

    private Long fileSize;

}
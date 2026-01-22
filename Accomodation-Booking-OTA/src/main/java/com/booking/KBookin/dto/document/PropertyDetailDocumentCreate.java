package com.booking.KBookin.dto.document;

import com.booking.KBookin.enumerate.property.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyDetailDocumentCreate implements Serializable {

    @NotNull(message = "Document type is required")
    private DocumentType type;

    @NotNull(message = "File is required")
    private MultipartFile file;
}
package com.booking.KBookin.dto.document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDocumentCreateRequest implements Serializable {
    @NotEmpty(message = "At least one document must be provided")
    @Valid
    List<PropertyDetailDocumentCreate> propertyDetailDocuments;
}

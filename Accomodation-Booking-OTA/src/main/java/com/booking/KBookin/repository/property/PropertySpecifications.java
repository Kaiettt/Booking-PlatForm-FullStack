package com.booking.KBookin.repository.property;

import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.enumerate.property.PropertyStatus;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecifications {
    public static Specification<Property> hasCity(String city) {
        return (root, query, cb) -> city == null ? null : cb.equal(root.get("city"), city);
    }

    public static Specification<Property> hasStatus(PropertyStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Property> isPublic() {
        return hasStatus(PropertyStatus.ACCEPTED);
    }
}

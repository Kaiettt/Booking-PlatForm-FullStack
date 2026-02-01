package com.booking.KBookin.repository.room;

import com.booking.KBookin.dto.room.RoomTypeFilterDTO;
import com.booking.KBookin.entity.room.RoomType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeSpecifications {

    public static Specification<RoomType> withFilters(Long propertyId, RoomTypeFilterDTO filter) {
        return (root, query, cb) -> {
            // Force DISTINCT to avoid duplicate RoomTypes due to collection joins
            assert query != null;
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // 1. Mandatory Property ID Filter
            predicates.add(cb.equal(root.get("property").get("id"), propertyId));

            if (filter == null) return cb.and(predicates.toArray(new Predicate[0]));

            // 2. Room Name (Case-insensitive Like)
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            // 3. Min Guests
            if (filter.getMinGuest() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxGuest"), filter.getMinGuest()));
            }

            // 4. Smoking Allowed
            if (filter.getSmokingAllowed() != null) {
                predicates.add(cb.equal(root.get("smokingAllowed"), filter.getSmokingAllowed()));
            }

            // 5. Faciltiies (Joins Many-to-Many)
            if (filter.getFacilities() != null && !filter.getFacilities().isEmpty()) {
                predicates.add(root.join("facilities").get("name").in(filter.getFacilities()));
            }

            // 5. Amenities (Joins Many-to-Many)
            if (filter.getFacilities() != null && !filter.getFacilities().isEmpty()) {
                predicates.add(root.join("amenities").get("id").in(filter.getAmenities()));
            }

            // 6. RatePlan Price & Prepayment
            // We join ratePlans once to use for multiple conditions
            if (filter.getMaxPrice() != null || filter.getPrepaymentTypes() != null) {
                var ratePlanJoin = root.join("ratePlans");

                if (filter.getMaxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(ratePlanJoin.get("price"), filter.getMaxPrice()));
                }

                if (filter.getPrepaymentTypes() != null && !filter.getPrepaymentTypes().isEmpty()) {
                    predicates.add(ratePlanJoin.get("prepaymentType").in(filter.getPrepaymentTypes()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
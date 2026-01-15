package com.booking.KBookin.repository.property.impl;

import com.booking.KBookin.dto.search.FilterSearchRequest;
import com.booking.KBookin.dto.search.SearchDetailPropertyDTO;
import org.springframework.stereotype.Component;

@Component
public class PropertySearchSqlBuilder {

        public String buildFilterQuery(FilterSearchRequest r, boolean isCountQuery) {
    
            StringBuilder sql = new StringBuilder();
    
            // SELECT
            if (!isCountQuery) {
                sql.append("""
                    SELECT 
                        p.id AS id,
                        MIN(rp.price) AS minPrice
                    """);
            } else {
                sql.append("""
                    SELECT COUNT(DISTINCT p.id)
                    """);
            }
    
            // FROM + JOIN
            sql.append("""
                FROM properties p
                JOIN room_types rt ON rt.property_id = p.id
                JOIN rate_plans rp ON rp.room_type_id = rt.id
                WHERE 1=1
            """);
    
            // City
            if (r.city() != null) {
                sql.append(" AND p.city = :city");
            }
    
            // Adults
            if (r.adults() != null) {
                sql.append(" AND rt.max_adults >= :adults");
            }
    
            // Children
            if (r.children() != null) {
                sql.append(" AND rt.max_children >= :children");
            }
    
            // Price
            if (r.minPrice() != null) {
                sql.append(" AND rp.price >= :minPrice");
            }
    
            if (r.maxPrice() != null) {
                sql.append(" AND rp.price <= :maxPrice");
            }

            // Property Type
            // Property Type (MULTI)
            if (r.propertyTypes() != null && !r.propertyTypes().isEmpty()) {
                sql.append(" AND p.\"type\" IN (:propertyType)");
            }



            // Rating
            if (r.minRating() != null) {
                sql.append(" AND p.avg_rating >= :minRating");
            }
    
            // Property Amenities Filter
            if (r.propertyAmenities() != null && !r.propertyAmenities().isEmpty()) {
                sql.append("""
                        AND EXISTS (
                            SELECT 1
                            FROM property_amenity_mappings pam
                            JOIN property_amenities pa ON pa.id = pam.amenity_id
                            WHERE pam.property_id = p.id
                              AND pa.name IN (:propertyAmenities)
                        )
                    """);
            }
    
    // Property Facilities Filter
            if (r.propertyFacilities() != null && !r.propertyFacilities().isEmpty()) {
                sql.append("""
                    AND EXISTS (
                        SELECT 1
                        FROM property_facility_mappings pfm
                        JOIN property_facilities pf ON pf.id = pfm.facility_id
                        WHERE pfm.property_id = p.id
                          AND pf.name IN (:propertyFacilities)
                    )
                """);
            }
    
            // Room Facilities Filter
            if (r.roomFacilities() != null && !r.roomFacilities().isEmpty()) {
                sql.append("""
                    AND EXISTS (
                        SELECT 1
                        FROM room_types_facilities rtf
                        JOIN room_facilities rf ON rf.id = rtf.facility_id
                        WHERE rtf.room_type_id = rt.id
                          AND rf.name IN (:roomFacilities)
                    )
                """);
            }
    
    
            // GROUP BY
            if (!isCountQuery) {
                sql.append(" GROUP BY p.id");
            }
    
            return sql.toString();
        }

    public String buildRoomFilterQuery(SearchDetailPropertyDTO r, boolean isCountQuery) {

        StringBuilder sql = new StringBuilder();

        if (!isCountQuery) {
            sql.append("""
            SELECT rt.id
        """);
        } else {
            sql.append("""
            SELECT COUNT(DISTINCT rt.id)
        """);
        }

        sql.append("""
        FROM room_types rt
        JOIN properties p ON p.id = rt.property_id
        JOIN rate_plans rp ON rp.room_type_id = rt.id
        WHERE 1=1
    """);

        sql.append(" AND p.id = :propertyId");

        if (r.adults() != null)
            sql.append(" AND rt.max_adults >= :adults");

        if (r.children() != null)
            sql.append(" AND rt.max_children >= :children");

        if (r.minPrice() != null)
            sql.append(" AND rp.price >= :minPrice");

        if (r.maxPrice() != null)
            sql.append(" AND rp.price <= :maxPrice");

        if (r.roomType() != null)
            sql.append(" AND rt.name = :roomType");

        // Room facilities filter
        if (r.roomFacilities() != null && !r.roomFacilities().isEmpty()) {
            sql.append("""
            AND EXISTS (
                SELECT 1
                FROM room_types_facilities rtf
                JOIN room_facilities rf ON rf.id = rtf.facility_id
                WHERE rtf.room_type_id = rt.id
                  AND rf.name IN (:roomFacilities)
            )
        """);
        }

        // Availability check
        if (r.checkingDate() != null && r.checkoutDate() != null) {
            sql.append("""
            AND NOT EXISTS (
                SELECT 1
                FROM room_inventory ri
                WHERE ri.room_type_id = rt.id
                  AND ri.date >= :checkingDate
                  AND ri.date < :checkoutDate
                  AND ri.available_rooms <= 0
            )
        """);
        }

        return sql.toString();
    }

}

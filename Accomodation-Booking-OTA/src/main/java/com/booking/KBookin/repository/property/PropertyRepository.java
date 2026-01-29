package com.booking.KBookin.repository.property;

import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.repository.projection.property.PropertyHostProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(
            value = """
        SELECT 
            p.id AS id, 
            MIN(rp.price) AS minPrice
        FROM properties p
        INNER JOIN room_types r ON r.property_id = p.id
        INNER JOIN rate_plans rp ON rp.room_type_id = r.id
        WHERE p.city = :city
          AND r.max_adults >= :adults
          AND r.max_children >= :children
        GROUP BY p.id
    """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM properties p
        INNER JOIN room_types r ON r.property_id = p.id
        WHERE p.city = :city
          AND r.max_adults >= :adults
          AND r.max_children >= :children
    """,
            nativeQuery = true
    )
    Page<PropertySearchResult> findPropertyWithMinPrice(   // ⬅ Sửa lại
                                                           @Param("city") String city,
                                                           @Param("adults") Integer adults,
                                                           @Param("children") Integer children,
                                                           Pageable pageable
    );


    @EntityGraph(attributePaths = {"host", "media", "amenities", "facilities"})
    @Query("SELECT p FROM Property p where p.id = :id")
    Optional<PropertyHostProjection> findHostViewPropertyById(@Param("id") Long id);
}
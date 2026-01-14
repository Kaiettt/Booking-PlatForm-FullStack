package com.booking.KBookin.repository.property.impl;

import com.booking.KBookin.dto.search.FilterSearchRequest;
import com.booking.KBookin.dto.search.SearchDetailPropertyDTO;
import com.booking.KBookin.entity.media.Media;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.property.PropertyAmenity;
import com.booking.KBookin.entity.property.PropertyFacility;
import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.entity.review.Review;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.repository.property.PropertySearchCustomRepository;
import com.booking.KBookin.repository.property.PropertySearchResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PropertySearchCustomRepositoryImpl implements PropertySearchCustomRepository {

    private final EntityManager em;
    private final PropertySearchSqlBuilder sqlBuilder;

    @Override
    public Page<PropertySearchResult> searchPropertyWithFilter(FilterSearchRequest r, Pageable pageable) {

        String sql = sqlBuilder.buildFilterQuery(r, false);
        String countSql = sqlBuilder.buildFilterQuery(r, true);

        Query query = em.createNativeQuery(sql, "PropertySearchResultMapping");
        Query countQuery = em.createNativeQuery(countSql);

        setParamsForPropertyFilter(query, r);
        setParamsForPropertyFilter(countQuery, r);

        // Paging
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<PropertySearchResult> list = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public Property searchRoomsWithFilter(SearchDetailPropertyDTO r) {
        String sql = sqlBuilder.buildRoomFilterQuery(r, false);
        Query nativeQuery = em.createNativeQuery(sql);
        setParamsForRoomsFilter(nativeQuery, r);

        @SuppressWarnings("unchecked")
        List<Number> ids = nativeQuery.getResultList();
        System.out.println("Break 1 - after native room filter");
        System.out.println("Break 1 - after native room filter");
        List<Long> roomTypeIds = ids.stream()
                .map(Number::longValue)
                .toList();
        Property property = em.find(Property.class, r.propertyId());
        property.getAmenities().size();
        property.getFacilities().size();
        property.getMedia().size();
        System.out.println("Break 2 - after em.find(Property)");
        if (roomTypeIds.isEmpty()) {
            property.setRoomTypes(Collections.emptyList());
            return property;
        }

        List<RoomType> roomTypes = em.createQuery("""
    SELECT DISTINCT rt FROM RoomType rt
    LEFT JOIN FETCH rt.ratePlans rp
    LEFT JOIN FETCH rp.perks
    WHERE rt.id IN :ids
""", RoomType.class)
                .setParameter("ids", roomTypeIds)
                .getResultList();


        em.createQuery("""
    SELECT rt FROM RoomType rt
    LEFT JOIN FETCH rt.facilities
    WHERE rt IN :roomTypes
""", RoomType.class)
                .setParameter("roomTypes", roomTypes)
                .getResultList();

        em.createQuery("""
    SELECT rt FROM RoomType rt
    LEFT JOIN FETCH rt.amenities
    WHERE rt IN :roomTypes
""", RoomType.class)
                .setParameter("roomTypes", roomTypes)
                .getResultList();

        em.createQuery("""
    SELECT rt FROM RoomType rt
    LEFT JOIN FETCH rt.media
    WHERE rt IN :roomTypes
""", RoomType.class)
                .setParameter("roomTypes", roomTypes)
                .getResultList();

// gán RoomTypes vào property
        property.setRoomTypes(roomTypes);

        System.out.println("Break 3 - after fetch roomTypes");


        // 4️⃣ Load Property-level collections (SEPARATE)
        loadPropertyCollections(property);
        List<Review> reviews = em.createQuery("""
    SELECT r FROM Review r
    JOIN FETCH r.user
    WHERE r.property = :property
""", Review.class)
                .setParameter("property", property)
                .getResultList();

        property.setReviews(new HashSet<>(reviews));
        System.out.println("Break 4 - after loadPropertyCollections");
        return property;
    }
    private void loadPropertyCollections(Property property) {

        Long propertyId = property.getId();
//
//        property.setAmenities(new HashSet<>(
//                em.createQuery("""
//                SELECT a FROM Property p
//                JOIN p.amenities a
//                WHERE p.id = :id
//            """, PropertyAmenity.class)
//                        .setParameter("id", propertyId)
//                        .getResultList()
//        ));
//
//        property.setFacilities(new HashSet<>(
//                em.createQuery("""
//                SELECT f FROM Property p
//                JOIN p.facilities f
//                WHERE p.id = :id
//            """, PropertyFacility.class)
//                        .setParameter("id", propertyId)
//                        .getResultList()
//        ));
//
//        property.setMedia(new HashSet<>(
//                em.createQuery("""
//                SELECT m FROM Property p
//                JOIN p.media m
//                WHERE p.id = :id
//            """, Media.class)
//                        .setParameter("id", propertyId)
//                        .getResultList()
//        ));

//        property.setReviews(new HashSet<>(
//                em.createQuery("""
//                SELECT r FROM Property p
//                JOIN p.reviews r
//                WHERE p.id = :id
//            """, Review.class)
//                        .setParameter("id", propertyId)
//                        .getResultList()
//        ));
    }


    private void setParamsForRoomsFilter(Query q, SearchDetailPropertyDTO r) {

        q.setParameter("propertyId", r.propertyId());

        if (r.adults() != null) q.setParameter("adults", r.adults());
        if (r.children() != null) q.setParameter("children", r.children());

        if (r.minPrice() != null) q.setParameter("minPrice", r.minPrice());
        if (r.maxPrice() != null) q.setParameter("maxPrice", r.maxPrice());

        if (r.minRating() != null) q.setParameter("minRating", r.minRating());

        if (r.roomType() != null) q.setParameter("roomType", r.roomType());

        if (r.roomFacilities() != null && !r.roomFacilities().isEmpty())
            q.setParameter("roomFacilities", r.roomFacilities());

        // Availability params
        if (r.checkingDate() != null) q.setParameter("checkingDate", r.checkingDate());
        if (r.checkoutDate() != null) q.setParameter("checkoutDate", r.checkoutDate());
    }


    private void setParamsForPropertyFilter(Query q, FilterSearchRequest r) {
        if (r.city() != null) q.setParameter("city", r.city());
        if (r.adults() != null) q.setParameter("adults", r.adults());
        if (r.children() != null) q.setParameter("children", r.children());
        if (r.minPrice() != null) q.setParameter("minPrice", r.minPrice());
        if (r.maxPrice() != null) q.setParameter("maxPrice", r.maxPrice());
        if (r.propertyTypes() != null && !r.propertyTypes().isEmpty()) {
            q.setParameter("propertyType", r.propertyTypes());
        }

        if (r.minRating() != null) q.setParameter("minRating", r.minRating());

        if (r.propertyAmenities() != null && !r.propertyAmenities().isEmpty())
            q.setParameter("propertyAmenities", r.propertyAmenities());

        if (r.propertyFacilities() != null && !r.propertyFacilities().isEmpty())
            q.setParameter("propertyFacilities", r.propertyFacilities());

        if (r.roomFacilities() != null && !r.roomFacilities().isEmpty())
            q.setParameter("roomFacilities", r.roomFacilities());
    }

}

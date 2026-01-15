package com.booking.KBookin.entity.media;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.room.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "media")
public class Media extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;

    @Column(columnDefinition = "TEXT")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

}

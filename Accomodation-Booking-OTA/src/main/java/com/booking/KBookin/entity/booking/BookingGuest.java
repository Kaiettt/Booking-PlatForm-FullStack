package com.booking.KBookin.entity.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingGuest {
    @Column(name = "guest_name", nullable = false)
    private String name;

    @Column(name = "guest_email", nullable = false)
    private String email;

    @Column(name = "guest_phone", nullable = false)
    private String phone;

    @Column(nullable = false)
    private String nationality;
}

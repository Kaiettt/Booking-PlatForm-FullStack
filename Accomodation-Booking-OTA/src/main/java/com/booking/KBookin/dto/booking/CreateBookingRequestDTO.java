package com.booking.KBookin.dto.booking;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CreateBookingRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOut;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @Size(max = 500, message = "Special request must not exceed 500 characters")
    private String specialRequest;

    @Valid
    @NotNull(message = "Guest information is required")
    private BookingGuestDTO guest;

    @Valid
    @NotEmpty(message = "Booking must contain at least one room item")
    private List<BookingItemRequestDTO> items;
}
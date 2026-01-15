package com.booking.KBookin.controller.booking;

import com.booking.KBookin.dto.booking.CancelBookingRequestDTO;
import com.booking.KBookin.dto.booking.CancellationResponse;
import com.booking.KBookin.dto.checkin.CheckoutResponse;
import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.checkin.CheckinResponse;
import com.booking.KBookin.service.booking.BookingService;
import com.booking.KBookin.service.booking.CancelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private BookingService bookingService;
    private CancelService cancelService;
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bookingService.createBooking(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.bookingService.getBookingById(id));
    }
    @PreAuthorize("hasRole('HOST')")
    @PostMapping("/check-in/{bookingId}")
    public ResponseEntity<CheckinResponse> checkin(@PathVariable("bookingId") Long bookingId){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bookingService.handleCheckin(bookingId));
    }
    @PostMapping("/check-out/{bookingId}")
    public ResponseEntity<CheckoutResponse> checkout(@PathVariable("bookingId") Long bookingId){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bookingService.handleCheckout(bookingId));
    }

    @PostMapping("/cancels")
    public ResponseEntity<CancellationResponse> cancelBooking(@RequestBody @Valid CancelBookingRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cancelService.cancelBooking(request));
    }
}

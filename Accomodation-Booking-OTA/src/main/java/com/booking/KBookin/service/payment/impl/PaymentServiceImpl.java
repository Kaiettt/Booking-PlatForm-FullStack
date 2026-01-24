package com.booking.KBookin.service.payment.impl;

import com.booking.KBookin.config.Common;
import com.booking.KBookin.dto.payment.PaymentResponse;
import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.payment.Payment;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.enumerate.payment.PaymentStatus;
import com.booking.KBookin.exception.VerificationException;
import com.booking.KBookin.kafka.producer.email.EmailBookingSuccessProducer;
import com.booking.KBookin.mapper.booking.BookingMapper;
import com.booking.KBookin.mapper.payment.PaymentMapper;
import com.booking.KBookin.repository.booking.BookingRepository;
import com.booking.KBookin.repository.payment.PaymentRepository;
import com.booking.KBookin.service.payment.PaymentProvider;
import com.booking.KBookin.service.payment.PaymentService;
import com.booking.KBookin.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final List<PaymentProvider> providers;
    private final RoomService roomService;
    private final EmailBookingSuccessProducer emailProducer;
    private final BookingMapper bookingMapper;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public String createPayment(String ipAddress, Map<String, String> params, PaymentMethod paymentMethod) {
        return findProvider(paymentMethod).createPaymentUrl(ipAddress, params);
    }

    @Override
    @Transactional
    public PaymentResponse handlePaymentCallBack(Map<String, String> params) {
        // 1. Dynamic Provider Selection (Fix A)
        PaymentProvider provider = providers.stream()
                .filter(p -> p.canHandle(params))
                .findFirst()
                .orElseThrow(() -> new VerificationException("No provider found to handle this callback"));

        // 2. Signature Check
        if (!provider.verifySignature(params)) {
            throw new VerificationException("Security violation: Invalid signature from " + provider.getMethod());
        }

        // 3. Load Context using Optional (Fix C)
        String txnRef = params.get(Common.TXN_REF);
        Booking booking = bookingRepository.findByBookingReference(txnRef);
        if(booking == null){
            new EntityNotFoundException("Booking not found: " + txnRef);
        }

        // 4. Process Result
        PaymentStatus status = provider.getStatus(params);
        Payment payment = mapToPayment(params, booking, status, provider.getMethod());

        if (status == PaymentStatus.PAID) {
            booking.handlePaymentSuccess();
        } else {
            booking.handlePaymentFailed();
        }

        // 5. Persistence (Fix B: bookingRepository.save is now implicit via @Transactional)
        Payment savedPayment = paymentRepository.save(payment);

        // 6. Post-processing
        roomService.releaseAllRoomLock(booking.getUser().getId());

        if (status == PaymentStatus.PAID) {
            emailProducer.sendBookingSuccessEvent(bookingMapper.toResponse(booking));
        }

        return paymentMapper.toResponse(savedPayment);
    }

    private PaymentProvider findProvider(PaymentMethod method) {
        return providers.stream()
                .filter(p -> p.getMethod() == method)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provider not supported: " + method));
    }

    private Payment mapToPayment(Map<String, String> params, Booking booking, PaymentStatus status, PaymentMethod method) {
        // BigDecimal Precision (Fix D)
        BigDecimal amount = new BigDecimal(params.get(Common.AMOUNT))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return Payment.builder()
                .booking(booking)
                .transactionId(params.get(Common.TRANSACTION_NO))
                .bankCode(params.get(Common.BANK_CODE))
                .amount(amount)
                .method(method) // Dynamic method
                .status(status)
                .payDate(LocalDateTime.now())
                .build();
    }
}
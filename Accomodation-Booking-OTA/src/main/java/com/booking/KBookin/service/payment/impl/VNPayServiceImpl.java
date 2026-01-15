package com.booking.KBookin.service.payment.impl;

import com.booking.KBookin.config.Common;
import com.booking.KBookin.config.VNPayConfig;
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
import com.booking.KBookin.service.payment.PaymentService;
import com.booking.KBookin.service.room.RoomService;
import com.booking.KBookin.util.PaymentUtil;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AllArgsConstructor
@Service
public class VNPayServiceImpl implements PaymentService {
    private BookingRepository bookingRepository;
    private PaymentMapper paymentMapper;
    private PaymentRepository paymentRepository;
    private RoomService roomService;
    private BookingMapper bookingMapper;
    private EmailBookingSuccessProducer emailBookingSuccessProducer;
    private boolean isValidSignature(Map<String, String> params) {
        String secureHash = params.get(Common.SECURE_HASH);
        Map<String, String> sorted = new TreeMap<>(params);
        sorted.remove(Common.SECURE_HASH);

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(entry.getKey()).append('=').append(entry.getValue());
        }

        String computedHash = PaymentUtil.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        return computedHash.equals(secureHash);
    }

    private Payment buildPaymentFromParams(Map<String, String> params, Booking booking) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTransactionId(params.get(Common.TRANSACTION_NO));
        payment.setBankCode(params.get(Common.BANK_CODE));
        double amount = Double.parseDouble(params.get(Common.AMOUNT)) / 100.0;
        payment.setAmount(BigDecimal.valueOf(amount));
        payment.setMethod(PaymentMethod.VN_PAY);

        String payDateStr = params.get(Common.PAY_DATE);
        if (payDateStr != null && !payDateStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            payment.setPayDate(LocalDateTime.parse(payDateStr, formatter));
        }

        return payment;
    }

    @Override
    public String createPayment(String ipAddr, Map<String, String> params) {
        String vnp_Version = VNPayConfig.vnp_version;
        String vnp_Command = "pay";
        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_IpAddr = ipAddr;
        int amount = Integer.parseInt(params.get("amount")) * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", params.get("vnp_OrderInfo"));
        vnp_Params.put("vnp_OrderType", params.get("ordertype"));
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", params.getOrDefault("language", "vn"));

        // ==========================
        // Thời gian VN (UTC+7)
        // ==========================
        Calendar cld = Calendar.getInstance();
        cld.add(Calendar.HOUR_OF_DAY, 7);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 120);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // ==========================
        // Sinh query & hash
        // ==========================
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        try {
            for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());
                    hashData.append(fieldName).append('=').append(encodedValue);
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=').append(encodedValue);

                    if (itr.hasNext()) {
                        hashData.append('&');
                        query.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }

        String vnp_SecureHash = PaymentUtil.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + vnp_SecureHash;

        return paymentUrl;
    }

    @Override
    public PaymentResponse handlePaymentCallBack(Map<String, String> params) {
        //Step 2 — Validate secure hash
        if (!isValidSignature(params)) {
            throw new VerificationException("Invalid signature");
        }
        // Step 3 — Find booking
        String txnRef = params.get(Common.TXN_REF);
        Booking booking = bookingRepository.findByBookingReference(txnRef);
        if (booking == null) {
            throw new EntityExistsException("Booking not found: " + txnRef);
        }
        // Step 4 — Create Payment
        Payment payment = buildPaymentFromParams(params, booking);
        // Step 5 — Update status
        String responseCode = params.get(Common.RESPONSE_CODE);
        String transactionStatus = params.get(Common.TRANSACTION_STATUS);
        String payementSucessCode = VNPayConfig.payment_sucess_code;
        if (payementSucessCode.equals(responseCode) && payementSucessCode.equals(transactionStatus)) {
            payment.setStatus(PaymentStatus.PAID);
            booking.handlePaymentSuccess();
        }
        else {
            payment.setStatus(PaymentStatus.FAILED);
            booking.handlePaymentFailed();
        }
        Payment savedPayment = paymentRepository.save(payment);
        bookingRepository.save(booking);
            this.roomService.releaseAllRoomLock(booking.getUser().getId());
        this.emailBookingSuccessProducer.sendBookingSuccessEvent(this.bookingMapper.toResponse(booking));
        return this.paymentMapper.toResponse(savedPayment);
    }
}

package com.booking.KBookin.service.payment;

import com.booking.KBookin.dto.payment.PaymentResponse;

import java.util.Map;

public interface PaymentService {
    String createPayment(String ipAddress, Map<String, String> params);

    PaymentResponse handlePaymentCallBack(Map<String, String> params);
}

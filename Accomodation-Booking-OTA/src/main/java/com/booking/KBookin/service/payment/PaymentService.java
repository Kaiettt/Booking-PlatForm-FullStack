package com.booking.KBookin.service.payment;

import com.booking.KBookin.dto.payment.PaymentResponse;
import com.booking.KBookin.enumerate.payment.PaymentMethod;

import java.util.Map;

public interface PaymentService {
    String createPayment(String ipAddress, Map<String, String> params, PaymentMethod paymentMethod);

    PaymentResponse handlePaymentCallBack(Map<String, String> params);
}

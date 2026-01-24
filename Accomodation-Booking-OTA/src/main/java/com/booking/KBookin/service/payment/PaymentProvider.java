package com.booking.KBookin.service.payment;

import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.enumerate.payment.PaymentStatus;

import java.util.Map;

public interface PaymentProvider {
    PaymentMethod getMethod();
    boolean canHandle(Map<String, String> params); // New: Identifies the provider
    String createPaymentUrl(String ipAddress, Map<String, String> params);
    boolean verifySignature(Map<String, String> params);
    PaymentStatus getStatus(Map<String, String> params);
}
package com.booking.KBookin.service.payment.impl;

import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.enumerate.payment.PaymentStatus;
import com.booking.KBookin.service.payment.PaymentProvider;

import java.util.Map;

public class PayPalProvider implements PaymentProvider {
    @Override
    public PaymentMethod getMethod() {
        return null;
    }

    @Override
    public boolean canHandle(Map<String, String> params) {
        return false;
    }

    @Override
    public String createPaymentUrl(String ipAddress, Map<String, String> params) {
        return "";
    }

    @Override
    public boolean verifySignature(Map<String, String> params) {
        return false;
    }

    @Override
    public PaymentStatus getStatus(Map<String, String> params) {
        return null;
    }
}

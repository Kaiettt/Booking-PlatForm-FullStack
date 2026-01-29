package com.booking.KBookin.service.payment.impl;

import com.booking.KBookin.config.Common;
import com.booking.KBookin.config.VNPayConfig;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.enumerate.payment.PaymentStatus;
import com.booking.KBookin.service.payment.PaymentProvider;
import com.booking.KBookin.util.PaymentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
public class VNPayProvider implements PaymentProvider {

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.VN_PAY;
    }

    @Override
    public String createPaymentUrl(String ipAddress, Map<String, String> params) {
        String vnp_TxnRef = params.get("vnp_TxnRef");
        int amount = Integer.parseInt(params.get("amount")) * 100;

        Map<String, String> vnp_Params = new TreeMap<>(); // Use TreeMap for auto-sorting
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_version);
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", params.get("vnp_OrderInfo"));
        vnp_Params.put("vnp_OrderType", params.get("ordertype"));
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", ipAddress);
        vnp_Params.put("vnp_Locale", params.getOrDefault("language", "vn"));

        // Use Java 8+ Date/Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ZonedDateTime nowVN = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        vnp_Params.put("vnp_CreateDate", nowVN.format(formatter));
        vnp_Params.put("vnp_ExpireDate", nowVN.plusMinutes(15).format(formatter));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        vnp_Params.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                String encodedKey = URLEncoder.encode(key, StandardCharsets.US_ASCII);
                String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII);
                if (hashData.length() > 0) hashData.append('&');
                hashData.append(key).append('=').append(encodedValue);

                if (query.length() > 0) query.append('&');
                query.append(encodedKey).append('=').append(encodedValue);
            }
        });

        String vnp_SecureHash = PaymentUtil.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        return VNPayConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    @Override
    public boolean verifySignature(Map<String, String> params) {
        String secureHash = params.get(Common.SECURE_HASH);
        Map<String, String> sorted = new TreeMap<>(params);
        sorted.remove(Common.SECURE_HASH);

        StringBuilder hashData = new StringBuilder();
        sorted.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                if (hashData.length() > 0) hashData.append('&');
                hashData.append(key).append('=').append(value);
            }
        });

        String computedHash = PaymentUtil.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        return computedHash.equals(secureHash);
    }

    @Override
    public PaymentStatus getStatus(Map<String, String> params) {
        String responseCode = params.get(Common.RESPONSE_CODE);
        return VNPayConfig.payment_sucess_code.equals(responseCode)
                ? PaymentStatus.PAID : PaymentStatus.FAILED;
    }
    @Override
    public boolean canHandle(Map<String, String> params) {
        // VNPay callbacks always include vnp_TmnCode or vnp_TxnRef
        return params.containsKey("vnp_TmnCode") || params.containsKey(Common.TXN_REF);
    }
}
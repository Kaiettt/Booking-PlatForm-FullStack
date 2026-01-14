package com.booking.KBookin.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {

    public static String vnp_TmnCode;
    public static String vnp_HashSecret;
    public static String vnp_PayUrl;
    public static String vnp_ReturnUrl;
    public static String vnp_IpnUrl;
    public static String vnp_version = "2.1.0";
    public static String  payment_sucess_code = "00";
    @Value("${vnpay.tmnCode}")
    public void setVnpTmnCode(String code) {
        VNPayConfig.vnp_TmnCode = code;
    }

    @Value("${vnpay.hashSecret}")
    public void setVnpHashSecret(String secret) {
        VNPayConfig.vnp_HashSecret = secret;
    }

    @Value("${vnpay.url}")
    public void setVnpPayUrl(String url) {
        VNPayConfig.vnp_PayUrl = url;
    }

    @Value("${vnpay.returnUrl}")
    public void setVnpReturnUrl(String url) {
        VNPayConfig.vnp_ReturnUrl = url;
    }

    @Value("${vnpay.ipnUrl}")
    public void setVnpIpnUrl(String url) {
        VNPayConfig.vnp_IpnUrl = url;
    }
}

package com.booking.KBookin.controller.payment;

import com.booking.KBookin.dto.payment.PaymentResponse;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.service.payment.PaymentService;
import com.booking.KBookin.util.PaymentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPaymentURL(
            HttpServletRequest req,
            @RequestParam Map<String, String> params) throws Exception {

        String paymentUrl = paymentService.createPayment(
                PaymentUtil.getIpAddress(req),
                params, PaymentMethod.VN_PAY
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment URL created successfully");
        response.put("paymentUrl", paymentUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/call-back")
    public ResponseEntity<PaymentResponse> handlePaymentCallback(@RequestParam Map<String,String> params){
        return ResponseEntity.ok(this.paymentService.handlePaymentCallBack(params));
    }
}

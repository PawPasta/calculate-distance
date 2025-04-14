package com.pawpasta.org.controller;

import com.pawpasta.org.config.VNPayConfig;
import com.pawpasta.org.model.PaymentRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) throws UnsupportedEncodingException {

        double baseFee = 10000; //10,000VND
        double distance = paymentRequest.getDistance();
        double distanceFee = calculateDistanceFee (distance);
        double totalFee = baseFee + distanceFee;

        long amount = Math.round(totalFee);


        String vnpayUrl = createVNPayUrl(amount, paymentRequest, request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("distance", distance);
        response.put("totalPrice", amount);
        response.put("paymentUrl", vnpayUrl);

        return ResponseEntity.ok(response);
    }

    private double calculateDistanceFee(double distance) {
        if (distance <= 5000) {
            return distance * 0.07;
        } else if (distance <= 20000) {
            return distance * 0.05;
        } else {
            return distance * 0.03;
        }
    }

    private String createVNPayUrl(long amount, PaymentRequest paymentRequest, HttpServletRequest request)
            throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Payment for delivery service: " +
                paymentRequest.getSource() + " to " + paymentRequest.getDestination();
        String orderType = "other";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Convert to VND with no decimal places
        vnp_Params.put("vnp_CurrCode", "VND");

        // Generate current date for VNPay format
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");

        // Return URL after payment is processed
        vnp_Params.put("vnp_ReturnUrl", "http://your-domain.com/api/payment/vnpay-callback");

        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        // Expiry date (optional)
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Build URL query string
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }



}
package org.example.PolicyMode;

import java.math.BigDecimal;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 16:27
 */
public class Main {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        paymentService.payment(new WechatPaymentStrategy(), new BigDecimal(100));
    }
}

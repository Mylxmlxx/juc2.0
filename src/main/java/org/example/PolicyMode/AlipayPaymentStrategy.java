package org.example.PolicyMode;

import java.math.BigDecimal;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 16:24
 */
public class AlipayPaymentStrategy implements PaymentStrategy {
    @Override
    public void payment(BigDecimal bigDecimal) {
        System.out.println("ali: " + bigDecimal);
    }
}

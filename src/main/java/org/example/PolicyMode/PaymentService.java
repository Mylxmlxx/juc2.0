package org.example.PolicyMode;

import java.math.BigDecimal;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 16:25
 */
public class PaymentService {
    public void payment(PaymentStrategy strategy, BigDecimal bigDecimal) {
        strategy.payment(bigDecimal);
    }
}

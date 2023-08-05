package org.example.PolicyMode;

import java.math.BigDecimal;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 16:23
 */
@FunctionalInterface
public interface PaymentStrategy {
    void payment(BigDecimal bigDecimal);
}

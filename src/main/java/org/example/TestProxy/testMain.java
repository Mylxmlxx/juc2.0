package org.example.TestProxy;

import java.util.HashSet;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/5/24 21:42
 */
public class testMain {
    public static void main(String[] args) {
        SmsService smsService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
        smsService.send("java");
        HashSet<Integer> set = new HashSet<>();

    }
}

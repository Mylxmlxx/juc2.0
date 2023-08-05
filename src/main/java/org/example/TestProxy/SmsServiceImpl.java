package org.example.TestProxy;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/5/24 21:40
 */
public class SmsServiceImpl implements SmsService {
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}
package org.example.ThreadPoolTest;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 9:57
 */
@Slf4j(topic = "pool")
public class ThreadPoolTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleAtFixedRate(() ->
        {
            log.debug("hello");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 1, 1, TimeUnit.SECONDS);


    }
}

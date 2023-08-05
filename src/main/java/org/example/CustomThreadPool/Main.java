package org.example.CustomThreadPool;

import java.util.concurrent.TimeUnit;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/31 11:24
 */
public class Main {
    public static void main(String[] args) {
        MyThreadPool threadPool = new MyThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            // 超时等待
            queue.offer(task, 500, TimeUnit.MILLISECONDS);
            // put 死等
            // 啥都不干 放弃
            // 抛异常
            // task.start() 调用者自己执行
        });
        for (int i = 0; i < 3; i++) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(j);
            });
        }
    }
}

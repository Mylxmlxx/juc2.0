package org.example.MyLock;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 17:07
 */
public class Main {
    public static MyLock myLock = new MyLock();
    public static int count = 0;

    public static void main(String[] args) {
        new Thread(() -> {
            myLock.lock();
            for (int i = 0; i < 100; i++) {
                count += 1;
                System.out.println(Thread.currentThread().getName() + ", count = " + count);
            }
            myLock.unlock();

        }).start();
        new Thread(() -> {
            myLock.lock();
            for (int i = 0; i < 100; i++) {
                count += 1;
                System.out.println(Thread.currentThread().getName() + ", count = " + count);
            }
            myLock.unlock();
        }).start();

    }
}

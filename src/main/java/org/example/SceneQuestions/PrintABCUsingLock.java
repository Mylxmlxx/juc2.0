package org.example.SceneQuestions;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABCUsingLock {
    private int times;
    private int state;
    private final Lock lock = new ReentrantLock();

    public PrintABCUsingLock(int times) {
        this.times = times;
    }

    public static void main(String[] args) throws Exception {
        PrintABCUsingLock usingLock = new PrintABCUsingLock(100);
        new Thread(() -> usingLock.print("A", 0)).start();
        new Thread(() -> usingLock.print("B", 1)).start();
        new Thread(() -> usingLock.print("C", 2)).start();

    }

    /**
     * using notify and wait
     * @param name
     * @param target
     */
    public void print(String name, int target) {
        for (int i = 0; i < times; ) {
            synchronized (lock) {
                while (state % 3 != target) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                state++;
                i++;
                System.out.println(Thread.currentThread().getName() + " " + name);
                lock.notifyAll();
            }
        }
    }
}

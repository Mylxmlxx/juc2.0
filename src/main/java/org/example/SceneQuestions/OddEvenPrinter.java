package org.example.SceneQuestions;

public class OddEvenPrinter {
    static final Object lock = new Object();
    private static final int limit = 100;
    private static volatile int count = 1;

    public static void main(String[] args) {
        new Thread(OddEvenPrinter::print, "odd").start();
        new Thread(OddEvenPrinter::print, "even").start();
    }

    public static void print() {
        synchronized (lock) {
            while (count < limit) {
                System.out.println(Thread.currentThread().getName() + ":" + count++);
                lock.notify();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

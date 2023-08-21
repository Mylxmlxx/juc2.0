package org.example.SceneQuestions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABCUsingLockCondition {
    private static final int times = 100;
    private static volatile int state;
    public static Lock lock = new ReentrantLock();

    public static Condition conditionA = lock.newCondition();
    public static Condition conditionB = lock.newCondition();
    public static Condition conditionC = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> print("A", 0, conditionA, conditionB)).start();
        new Thread(() -> print("B", 1, conditionB, conditionC)).start();
        new Thread(() -> print("C", 2, conditionC, conditionA)).start();
    }

    public static void print(String name, int tartGetState, Condition current, Condition next) {
        for (int i = 0; i < times; ) {
            lock.lock();
            try {
                while (state % 3 != tartGetState) {
                    current.await();
                }
                System.out.println(Thread.currentThread().getName() + "   " + name);
                state++;
                i++;
                next.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

package org.example.SceneQuestions;

public class NumAndLetterPrinter {
    private static final Object lock = new Object();
    private static char c = 'A';

    public static void main(String[] args) throws Exception {
        new Thread(NumAndLetterPrinter::printer, "num").start();
        new Thread(NumAndLetterPrinter::printer, "letter").start();
    }

    private static void printer() {
        synchronized (lock) {
            for (int i = 0; i < 26; i++) {
                if (Thread.currentThread().getName().equals("num")) {
                    System.out.println(Thread.currentThread().getName() + "   " + i);
                    //唤醒其他线程
                    lock.notify();
                    try {
                        //释放当前锁
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else {

                    System.out.println(Thread.currentThread().getName() + "   " + (char) (c + i));
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}

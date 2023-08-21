package org.example.MyLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/8/5 16:37
 */
public class MyLock implements Lock {
    Sync sync = new Sync();

    @Override
    public void lock() {
        sync.tryAcquire(1);
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "尝试获取锁，锁的状态为: " + thread.getState());
    }

    //可被打断的获取锁
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    //
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            Thread currentThread = Thread.currentThread();
            //共享变量
            int state = getState();
            if (state == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, arg)) {
                    setExclusiveOwnerThread(currentThread);
                    System.out.println(currentThread.getName() + "获得锁");
                }
                return true;
            }
            return false;
        }


        @Override
        protected boolean tryRelease(int arg) {
            int state = getState() - arg;
            boolean free = false;
            // 如果不是当前线程持有锁
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException("不要尝试释放别人的锁");
            }
            if (state == 0) {
                free = true;
                setExclusiveOwnerThread(null);
                System.out.println(Thread.currentThread().getName() + "释放锁");
            }
            setState(state);
            return free;
        }
    }
}

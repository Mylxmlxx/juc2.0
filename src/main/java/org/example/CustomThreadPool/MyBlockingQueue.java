package org.example.CustomThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/31 9:55
 */
@Slf4j
public class MyBlockingQueue<T> {
    private final Deque<T> queue = new ArrayDeque<>();

    private final ReentrantLock lock = new ReentrantLock();


    // 生产者条件变量
    private final Condition fullWaitSet = lock.newCondition();

    // 消费者条件变量
    private final Condition emptyWaitSet = lock.newCondition();

    private final int capacity;

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
    }


    /**
     * 获取任务执行（超市）
     *
     * @param timeout
     * @param unit
     * @return
     */
    public T poll(long timeout, TimeUnit unit) {
        T t = null;
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                if (nanos <= 0) {
                    log.debug("任务队列为空，等待超时，返回null");
                    return null;
                }
//                log.debug("poll消费者等待任务ing...");
                nanos = emptyWaitSet.awaitNanos(nanos);
            }

            t = queue.removeFirst();
            fullWaitSet.signal();
            return t;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }

    /**
     * 获取任务执行（永久等待）
     *
     * @return
     */
    public T take() {
        T t = null;
        lock.lock();
        try {
            // 1. 判断队列是否为空
            while (queue.isEmpty()) {
                try {
                    // 2. 如果为空，那么消费者线程需要等待
                    emptyWaitSet.await();
                    log.info("take消费者线程等待任务ing...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t = queue.removeFirst();
            // 3. 唤醒生产者线程
            fullWaitSet.signal();
        } finally {
            lock.unlock();
        }
        return t;
    }

    /**
     * 添加任务（超市）
     *
     * @param t
     * @param timeout
     * @param unit
     * @return
     */
    public boolean offer(T t, long timeout, TimeUnit unit) {
        lock.lock();
        try {
            //队列空了之后就开始等待，超时后返回false
            log.debug("等待加入任务队列 {}...", t);
            long nanos = unit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    if (nanos <= 0) {
                        log.debug("任务添加失败，等待超时，返回false");
                        return false;
                    }

                    nanos = fullWaitSet.awaitNanos(nanos);

//                    log.debug("offer生产者等待ing...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.debug("任务被加入到队列之中:{}", t);
            queue.addLast(t);
            // 唤醒消费者线程
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 添加任务（死等）
     *
     * @param t
     */
    public void put(T t) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列");
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列:{}", t);
            queue.addLast(t);
            // 唤醒消费者线程
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }


    }

    /**
     * 尝试添加任务（拒绝策略）
     *
     * @param rejectPolicy
     * @param t
     */
    public void tryPut(RejectPolicy rejectPolicy, T t) {
        lock.lock();

        try {
            if (queue.size() == capacity) {
//                log.debug("等待加入队列...");
                rejectPolicy.reject(this, t);
            } else {
                log.debug("任务被加入到队列之中:{}", t);
                queue.addLast(t);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }

}

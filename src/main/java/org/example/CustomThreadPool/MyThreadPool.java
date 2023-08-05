package org.example.CustomThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/31 10:15
 */
@Slf4j(topic = "c.MyThreadPool")
public class MyThreadPool {
    private final MyBlockingQueue<Runnable> workQueue;
    private final Set<Worker> workers = new HashSet<>();
    private final int coreSize;
    private final long timeout;
    private final TimeUnit timeUnit;


    private RejectPolicy<Runnable> rejectPolicy;

    public MyThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.workQueue = new MyBlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    public MyThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.workQueue = new MyBlockingQueue<>(queueCapacity);
    }

    /**
     * 执行任务
     * @param task
     */
    public void execute(Runnable task) {

        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增worker{},{}", worker, task);
                workers.add(worker);
                worker.start();
            } else {

//                workQueue.put(task);
                workQueue.tryPut(rejectPolicy, task);
            }

        }


    }

    /**
     * 线程
     */
    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        /**
         * 只要任务队列不为空，一直获取任务执行
         */
        @Override
        public void run() {
            // worker需要反复执行任务，用take不断从队头获取元素
            while (task != null || (task = workQueue.poll(timeout, timeUnit)) != null) {
                if (workers.size() == 0) break;
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker被移除{}", this);
                workers.remove(this);
            }
        }
    }

}

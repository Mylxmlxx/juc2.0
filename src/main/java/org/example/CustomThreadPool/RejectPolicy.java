package org.example.CustomThreadPool;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/31 13:29
 */
@FunctionalInterface
public interface RejectPolicy<T> {
    void reject(MyBlockingQueue<T> queue , T t);
}

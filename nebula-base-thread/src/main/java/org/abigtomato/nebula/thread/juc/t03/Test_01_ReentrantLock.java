package org.abigtomato.nebula.thread.juc.t03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock重入锁机制
 */
public class Test_01_ReentrantLock {

    Lock lock = new ReentrantLock();

    void m1() {
        try {
            // 加锁
            lock.lock();
            for (int i = 0; i < 10; ++i) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    void m2() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + ":m2()");
        } catch (Exception e) {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final Test_01_ReentrantLock t = new Test_01_ReentrantLock();

        new Thread(() -> t.m1()).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> t.m2()).start();
    }

}

package org.abigtomato.nebula.thread.juc.t03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的可打断机制
 *
 * 线程生命周期：
 *      等待 -> 执行 -> 阻塞 -> 销毁
 * 线程的阻塞状态：
 *      普通阻塞，等待队列，锁池队列
 * 普通阻塞：
 *      调用sleep(10000)方法进入的阻塞状态，可以被打断，thread.interrupt()方法可以打断阻塞状态，抛出异常
 * 等待队列：
 *      调用wait()方法进入的阻塞状态，只能使用notify唤醒，无法被打断
 * 锁池队列：
 *      无法获得锁标记时进入的阻塞状态，不是所有存在锁池队列中的线程都可被打断
 *      使用ReentrantLock的lock方法获取锁标记的时候，如果需要阻塞等待锁标记，无法被打断
 *      使用ReentrantLock的lockInterruptibly方法获取锁标记的时候，如果需要阻塞等待，可以被打断
 */
public class Test_03_ReentrantLock {

    Lock lock = new ReentrantLock();

    void m1() {
        try {
            lock.lock();
            for(int i=0; i<10; ++i) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void m2() {
        try {
            /**
             * 可尝试打断机制：
             * 阻塞等待锁，可以被其他的线程打断阻塞状态，打断后会抛出异常
             */
            lock.lockInterruptibly();
            System.out.println("void m2() method");
        } catch (Exception e) {
            System.out.println("void m2() method interrupted");
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final Test_03_ReentrantLock t = new Test_03_ReentrantLock();

        Thread t1 = new Thread(() -> t.m1());
        Thread t2 = new Thread(() -> t.m2());

        try {
            t1.start();
            TimeUnit.SECONDS.sleep(1);
            t2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
            /**
             * main线程打断t2线程的阻塞状态
             */
            t2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

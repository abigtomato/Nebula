package org.abigtomato.nebula.thread.juc.t03;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁机制
 */
public class Test_04_ReentrantLock {

    private static class TestReentrantlock implements Runnable {

        /**
         * 公平锁：
         * 当持有锁的线程释放锁，cpu会让所有阻塞状态的线性争夺锁资源，先标记的先获得cpu执行权
         * 公平锁就是让cpu从阻塞队列的队头获取线程给予执行权(先前先出，等待时间越长的线程执行权越高)
         */
        ReentrantLock lock = new ReentrantLock(true);

        @Override
        public void run() {
            for(int i=0; i<5; ++i) {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                } finally {
                    lock.unlock();
                }
            }
        }

    }

    private static class TestSynchronized implements Runnable {

        @Override
        public void run() {
            for(int i=0; i<5; ++i) {
                synchronized (this) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                }
            }
        }

    }

    public static void main(String[] args) {
        TestReentrantlock test = new TestReentrantlock();
//        TestSynchronized test = new TestSynchronized();
        new Thread(test, "thread-1").start();
        new Thread(test, "thread-2").start();
    }

}

package org.abigtomato.nebula.thread.juc.t01;

/**
 * 线性加锁的目的：
 * 保证操作的原子性
 */
public class T_03_Synchronized implements Runnable {

    private int count = 0;

    /**
     * 保证当前方法内部的操作不可再分(由同一个线程执行到底)
     */
    @Override
    public synchronized void run() {
        System.out.println(Thread.currentThread().getName() + " count=" + count++);
    }

    public static void main(String[] args) {
        T_03_Synchronized test_03 = new T_03_Synchronized();
        for (int i = 0; i < 50; ++i) {
            new Thread(test_03, "Thread-" + i).start();
        }
    }
}

package org.abigtomato.nebula.thread.juc.t01;

import java.util.concurrent.TimeUnit;

/**
 * 锁对象变更问题
 * 同步代码一旦加锁后，会有一个临时的锁引用在栈帧中指向锁对象，和真实的引用无关
 * 在锁未释放之前，修改锁对象的引用，不会影响同步代码的执行
 */
public class T_13_Synchronized {

    private Object o = new Object();

    void m() {
        System.out.println();
        synchronized (o) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + o);
        }
    }

    public static void main(String[] args) {
        final T_13_Synchronized t = new T_13_Synchronized();

        new Thread(() -> t.m(), "thread1").start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> t.m(), "thread2");
        t.o = new Object();
        thread.start();
    }
}

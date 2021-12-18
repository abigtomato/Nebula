package org.abigtomato.nebula.thread.juc.t01;

import java.util.concurrent.TimeUnit;

/**
 * 当同步方法发生异常时自动释放锁资源，不会影响其他线程执行
 */
public class T_08_Synchronized {

    private int i = 0;

    synchronized void m() {
        System.out.println(Thread.currentThread().getName() + "- start");
        while (true) {
            System.out.println(Thread.currentThread().getName() + "-" + ++i);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 当前线程运行到此处自杀性异常时会自动释放锁
            if (i == 5) {
                i = 1 / 0;
            }
        }
    }

    public static void main(String[] args) {
        T_08_Synchronized t = new T_08_Synchronized();
        new Thread(() -> t.m(), "thread-01").start();
        new Thread(() -> t.m(), "thread-02").start();
    }
}

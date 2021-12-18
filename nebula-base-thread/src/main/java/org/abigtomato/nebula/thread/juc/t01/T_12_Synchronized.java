package org.abigtomato.nebula.thread.juc.t01;

/**
 * 同步方法和同步代码块比较(同步粒度问题)
 * 在商业开发中，尽量避免使用同步方法而使用同步代码块
 */
public class T_12_Synchronized {

    synchronized void m1() {
        System.out.println("前置逻辑");
        System.out.println("同步逻辑");
        System.out.println("后置逻辑");
    }

    void m2() {
        System.out.println("前置逻辑");
        synchronized (this) {
            System.out.println("同步逻辑");
        }
        System.out.println("后置逻辑");
    }
}

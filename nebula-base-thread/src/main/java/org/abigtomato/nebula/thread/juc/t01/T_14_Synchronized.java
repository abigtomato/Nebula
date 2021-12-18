package org.abigtomato.nebula.thread.juc.t01;

/**
 * 常量问题：
 * 在定义同步代码块时，不要通过常量对象作为锁对象
 *
 * @author 16798
 */
public class T_14_Synchronized {

    void m1() {
        // hello保存在堆区的常量池中
        String s1 = "hello";
        synchronized (s1) {
            System.out.println(Thread.currentThread().getName() + "m1()");
            while (true) {
            }
        }
    }

    void m2() {
        String s2 = "hello";
        synchronized (s2) {
            System.out.println(Thread.currentThread().getName() + "m2()");
            while (true) {
            }
        }
    }

    public static void main(String[] args) {
        final T_14_Synchronized t = new T_14_Synchronized();
        new Thread(() -> t.m1(), "thread1").start();
        new Thread(() -> t.m2(), "thread2").start();
    }
}

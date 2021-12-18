package org.abigtomato.nebula.thread.juc.t01;

import java.util.ArrayList;

/**
 * volatile关键字只能保证可见性，并不能保证原子性
 */
public class T_10_Volatile {

    volatile int count = 0;

    void m() {
        for (int i = 0; i < 10000; ++i) {
            count++;
        }
    }

    public static void main(String[] args) {
        final T_10_Volatile t = new T_10_Volatile();
        ArrayList<Thread> arrayList = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            arrayList.add(new Thread(t::m, "Thread:" + i));
        }

        for (Thread thread : arrayList) {
            thread.start();
        }

        // 将其他所有线程连接到main线程的当前位置，等所有线程执行完毕，才会继续执行main接下来的逻辑
        for (Thread thread : arrayList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(t.count);
    }
}

package org.abigtomato.nebula.thread.juc.t01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger原子操作类型：
 * 其中每个方法都是原子操作，可以保证线程安全
 */
public class T_11_AtomicInteger {

    AtomicInteger count = new AtomicInteger(0);

    void m() {
        for (int i = 0; i < 10000; ++i) {
            // ++ count;
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        final T_11_AtomicInteger t = new T_11_AtomicInteger();
        List<Thread> list = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            list.add(new Thread(() -> t.m(), "Thread:" + i));
        }

        for (Thread thread : list) {
            thread.start();
        }

        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(t.count.intValue());
    }

}

package org.abigtomato.nebula.thread.juc.t04;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 练习(wait + notify的方式实现生产者消费者模式)：
 * 自定义同步容器，容器容量上限为10，可以在多线程中应用，并保证数据线程安全
 */
public class Test_01_Wait_Notify<E> {

    private final LinkedList<E> linkedList = new LinkedList<>();
    private final int MAX = 10;
    private int count = 0;

    public synchronized int getCount() {
        return this.count;
    }

    public synchronized void put(E e) {
        while (linkedList.size() == MAX) {
            try {
                this.wait();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        linkedList.add(e);
        count++;

        this.notifyAll();
    }

    public synchronized E get() {
        while (linkedList.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        E e = linkedList.removeFirst();
        count--;

        this.notifyAll();

        return e;
    }

    public static void main(String[] args) {
        final Test_01_Wait_Notify t = new Test_01_Wait_Notify();
        for (int i = 0; i < 10; ++i) {
            new Thread(() -> {
                for (int j = 0; j < 5; ++j) {
                    System.out.println(t.get());
                }
            }, "consumer:" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2; ++i) {
            new Thread(() -> {
                for (int j = 0; j < 25; ++j) {
                    t.put("container value: " + j);
                }
            }, "producer:" + i).start();
        }
    }

}

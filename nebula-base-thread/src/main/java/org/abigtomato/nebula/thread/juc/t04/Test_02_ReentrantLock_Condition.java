package org.abigtomato.nebula.thread.juc.t04;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 练习(ReentrantLock + Condition方式实现生产者消费者模式)：
 * 自定义同步容器，容器容量上限为10，可以在多线程中应用，并保证数据线程安全
 */
public class Test_02_ReentrantLock_Condition<E> {

    private LinkedList<E> linkedList = new LinkedList<>();
    private final int MAX = 10;
    private int count = 0;

    private Lock lock = new ReentrantLock();
    /**
     * 重入锁的条件机制：
     * 通过锁对象生成条件，当条件满足时，触发事件：如加锁或解锁，等待或唤醒
     */
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public void put(E e) {
        try {
            lock.lock();

            while(linkedList.size() == MAX) {
                System.out.println(Thread.currentThread().getName() + "进入等待。。。");
                /**
                 * 生产线程进入阻塞队列，是否锁标记
                 */
                producer.await();
            }

            linkedList.add(e);
            count ++;

            /**
             * 唤醒所有在阻塞队列中的消费线程
             */
            consumer.signalAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public E get() {
        E res = null;
        try {
            lock.lock();

            while(linkedList.size() == 0) {
                System.out.println(Thread.currentThread().getName() + "进入等待。。。");
                consumer.await();
            }

            res = linkedList.removeFirst();
            count --;

            producer.signalAll();

            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return null;
    }

    public static void main(String[] args) {
        final Test_02_ReentrantLock_Condition<String> t = new Test_02_ReentrantLock_Condition<>();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName() + ":" + t.get());
                }
            }, "consumer: " + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    t.put("container value: " + j);
                }
            }, "producer: " + i).start();
        }

    }

}

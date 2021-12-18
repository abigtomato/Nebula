package org.abigtomato.nebula.thread.juc.t06;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * LinkedBlockingQueue
 */
public class Test_04_LinkedBlockingQueue {

    /**
     * 基于链表实现的阻塞队列
     */
    final BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
    final Random r = new Random();

    public static void main(String[] args) {
        final Test_04_LinkedBlockingQueue t = new Test_04_LinkedBlockingQueue();

        new Thread(() -> {
            while (true) {
                try {
                    /**
                     * put()，当队列容量满了，无法进行生产时，自动阻塞
                     */
                    t.queue.put("value:" + t.r.nextInt(1000));
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer").start();

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        /**
                         * take()，当队列容量为0，无法进行消费时，自动阻塞
                         */
                        System.out.println(Thread.currentThread().getName() +
                                "-" + t.queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "consumer" + i).start();
        }

    }

}

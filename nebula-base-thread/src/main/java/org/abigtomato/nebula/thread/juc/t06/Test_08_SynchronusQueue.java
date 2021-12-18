package org.abigtomato.nebula.thread.juc.t06;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * SynchronousQueue 同步队列
 */
public class Test_08_SynchronusQueue {

    /**
     * 同步队列，是一个容量为0的队列，是一个特殊的转移队列
     * 是必须先有消费线程等待，才能使用的队列
     */
    BlockingQueue<String> queue = new SynchronousQueue<>();

    public static void main(String[] args) {
        final Test_08_SynchronusQueue t = new Test_08_SynchronusQueue();

        /**
         * 消费线程等待数据生产
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + "-thread begin");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "-" + t.queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "output thread").start();

        try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        /**
         * add()方法，无阻塞，若没有消费线程阻塞等待数据，则抛出异常
         */
        t.queue.add("test add");

        try {
            /**
             * put()方法，有阻塞，若没有消费线程阻塞等待数据，则阻塞
             */
            t.queue.put("test put");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " queue size : " + t.queue.size());
    }

}

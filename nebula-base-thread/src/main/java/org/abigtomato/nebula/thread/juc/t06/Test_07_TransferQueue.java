package org.abigtomato.nebula.thread.juc.t06;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * LinkedTransferQueue 转移队列
 */
public class Test_07_TransferQueue {

    /**
     * LinkedTransferQueue 转移队列
     * 转移队列使用transfer方法实现数据的即时处理
     * 若是没有消费者，就阻塞
     */
    TransferQueue<String> queue = new LinkedTransferQueue<>();

    public static void main(String[] args) {
        final Test_07_TransferQueue t = new Test_07_TransferQueue();

        new Thread(() -> {
            try {
                /**
                 * transfer()是TransferQueue的特有方法，必须有消费者(take()方法的调用线程)存在
                 * 如果没有任何线程消费数据，transfer方法会陷入阻塞
                 * 一般用于处理即时消息
                 */
                t.queue.transfer("test string");

                /**
                 * add()方法使队列保存数据，不做阻塞等待
                 */
                t.queue.add("test string");
                System.out.println("transfer ok");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + "-output thread begin");
                    System.out.println(Thread.currentThread().getName() + "-" + t.queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "output thread").start();

    }

}

package org.abigtomato.nebula.thread.juc.t06;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ArrayBlockingQueue
 */
public class Test_05_ArrayBlockingQueue {

    /**
     * ArrayBlockingQueue 底层由数组实现的有界队列
     * 当容量不足的时候，触发自动阻塞的机制，根据调用API(add/put/offer)的不同，显示不同的特性
     */
    final BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

    public static void main(String[] args) {
        final Test_05_ArrayBlockingQueue t = new Test_05_ArrayBlockingQueue();
        for (int i = 0; i < 5; i++) {
            /**
             * add()方法在容量不足的时候，抛出异常
             */
            System.out.println("add method:" + t.queue.add("value" + i));

            try {
                /**
                 * put()方法在容量不足的时候，阻塞等待
                 */
				t.queue.put("put" + i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("put method : " + i);

            /**
             * 单参数offer()方法，不阻塞，容量不足的时候，返回false，当前新增的数据放弃
             */
            System.out.println("offer method:" + t.queue.offer("value" + i));
            try {
                /**
                 * 三参数重载的offer()方法，容量不足的时候，阻塞单位为timeunit的timeout时长
                 * 如果在阻塞时长内，有容量空闲，新增数据返回true，如果阻塞时长范围内，无容量空闲，放弃新增数据，返回false
                 */
                System.out.println("offer method:" +
                        t.queue.offer("value" + i, 1, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(t.queue);
    }

}

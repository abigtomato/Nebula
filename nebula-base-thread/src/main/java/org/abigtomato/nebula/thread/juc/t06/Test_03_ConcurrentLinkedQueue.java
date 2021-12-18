package org.abigtomato.nebula.thread.juc.t06;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue
 */
public class Test_03_ConcurrentLinkedQueue {

    public static void main(String[] args) {
        /**
         * 底层链表实现的同步队列
         */
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 10; i++) {
            /**
             * offer() -> 向queue队尾插入元素
             */
            queue.offer("value-" + i);
        }

        System.out.println(queue);
        System.out.println(queue.size());

        /**
         * peek() -> 查看queue中的首数据
         */
        System.out.println(queue.peek());
        System.out.println(queue.size());

        /**
         * poll() -> 获取queue中的首数据
         */
        System.out.println(queue.poll());
        System.out.println(queue.size());
    }

}

package org.abigtomato.nebula.thread.juc.t07;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 练习(并发容器实现)：
 * 启动若干线程，并行访问同一个容器中的数据，保证获取容器中数据时没有数据错误，且线程安全
 * 如售票，秒杀等业务
 */
public class Test_01 {

    static Queue<String> queue = new ConcurrentLinkedDeque<>();

    static {
        for (int i = 0; i < 10; i++) {
            queue.add("String" + i);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while(true) {
                    String str = queue.poll();
                    if(str == null) {
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + " - " + str);
                }
            }, "thread-" + i).start();
        }
    }

}

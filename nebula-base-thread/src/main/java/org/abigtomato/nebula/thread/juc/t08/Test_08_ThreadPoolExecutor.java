package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池特性 -> ThreadPoolExecutor
 */
public class Test_08_ThreadPoolExecutor {

    public static void main(String[] args) {
        /**
         * ThreadPoolExecutor -> 线程池底层实现，除ForkJoinPool外，其他常用线程池底层都是使用ThreadPoolExecutor实现的
         * 1.ThreadPoolExecutor属性：
         *  int corePoolSize -> 核心容量，创建线程池的时候，默认有多少线程，也是线程池保持的最少线程数
         *  int maximumPoolSize -> 最大容量，线程池最多有多少线程
         *  long keepAliveTime -> 生命周期，当线程空闲多久后自动回收，0表示永久存在
         *  TimeUnit unit -> 生命周期单位，为生命周期提供单位，如：秒，毫秒
         *  BlockingQueue<Runnable> workQueue -> 任务队列，阻塞队列，注：泛型必须是Runnable
         * 2.使用场景：
         *  默认提供的线程池不满足条件时使用
         *  如：初始线程数据4，最大线程数200，线程空闲周期30秒
         */
        ExecutorService service = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 6; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("任务执行：" + Thread.currentThread().getName());
                }
            });
        }

        System.out.println(service);

        service.shutdown();
        System.out.println(service.isTerminated());
        System.out.println(service.isShutdown());
        System.out.println(service);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();
        System.out.println(service.isTerminated());
        System.out.println(service.isShutdown());
        System.out.println(service);
    }

}

package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * FixedThreadPool -> 固定容量的线程池
 * 创建线程池的时候容量就已经固定，构造的时候，提供线程池最大容量
 */
public class Test_02_FixedThreadPool {

    public static void main(String[] args) {
        /**
         * Executor -> 线程池顶级接口
         *  提供的服务方法void execute(Runnable)是用于启动线程任务的
         *  调用者提供Runnable接口的实现，线程池通过线程执行这个Runnable，服务方法无返回值
         *
         * ExecutorService -> Executor接口的子接口，提供了一个新的服务方法Future submit(Runnable)，服务方法存在返回值
         *  Future submit(Runnable)不需要提供返回值
         *  Future submit(Callable)可以提供线程执行后的返回值
         *  Future是submit方法的返回值，代表未来，也就是线程执行结束后的一种结果，如返回值
         *
         * 线程池状态 ->
         *  1.Running：线程池正在执行中，活动状态
         *  2.ShuttingDown：线程池正在优雅关闭的过程中，一旦进入这个状态线程池不再接收新的任务，处理所有已接收的任务，处理完毕后，关闭线程池
         *  3.Terminated：线程池已经关闭
         *
         * Executors -> 工具类型，为Executor线程池提供工具方法，可以快速的提供若干种线程池，类似Arrays，Collections等工具类型
         *  线程池是一个进程级的重量级资源，默认的生命周期和JVM一致，当开启线程池后，直到JVM关闭为止，是线程池的默认生命周期
         *  如果手动调用shutdown方法，那么线程池执行所有的任务后，自动关闭
         *  开始 - 创建线程池
         *  结束 - JVM关闭或调用shutdown并处理完所有的任务
         *
         * FixedThreadPool -> 容量固定的线程池(活动状态和线程池容量是有上限的线程池)
         *  1.所有的线程池中，都有一个任务队列，使用的是BlockingQueue<Runnable>作为任务的载体
         *    当任务数量大于线程池容量的时候，没有被执行的任务保存在任务队列中，当线程池有空闲时自动从队列中取出任务执行
         *  2.使用场景：大多数情况下，使用的线程池，首选推荐FixedThreadPool，OS系统和硬件是有线程支持上限，不能随意的无限制提供线程
         *    线程池默认的容量上限是Integer.MAX_VALUE
         */
        ExecutorService service = Executors.newFixedThreadPool(5);

        /**
         * 线程池的容量为5，最多并行执行5个线程，还有一个线程等待位置空闲
         */
        for (int i = 0; i < 6; i++) {
            service.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " - test executor");
            });
        }
        System.out.println("线程池基本信息：" + service);

        /**
         * shutdown() -> 优雅关闭
         *  不是强行关闭线程池，回收线程池中的资源，而是不再处理新的任务，将已接收的任务处理完毕后再关闭
         */
        service.shutdown();
        System.out.println("是否已经结束，相当于回收了资源：" + service.isTerminated());
        System.out.println("是否已经关闭，是否调用过shutdown方法：" + service.isShutdown());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(service.isTerminated());
        System.out.println(service.isShutdown());
        System.out.println(service);
    }

}

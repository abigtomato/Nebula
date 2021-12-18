package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.*;

/**
 * Callable -> 允许返回值的线程启动接口
 * Future -> 线程执行完后返回的结果
 */
public class Test_03_Future {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(1);

        /**
         * Callable ->
         * 可执行接口，类似Runnable接口，也是可以启动一个线程的接口
         * 其中定义的方法是call，call方法的作用和Runnable中的run方法完全一致，不同的是call方法存在返回值
         * 和Runnable接口的选择，需要返回值或需要抛出异常时，使用 Callable，其他情况可任意选择
         *
         * Future<T> ->
         * 未来结果，代表线程任务执行结束后的结果，获取线程执行结果的方式是通过get方法获取的
         * get无参，阻塞等待线程执行结束，并得到结果
         * get 有参，阻塞固定时长，等待线程执行结束后的结果，如果在阻塞时长范围内，线程未执行结束，抛出异常
         */
        Future<String> future = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println("submit callable future");
                return Thread.currentThread().getName();
            }
        });

        System.out.println(future);
        System.out.println("查看线程的call方法是否执行结束：" + future.isDone());

        System.out.println("获取线程返回值的内容：" + future.get());
        System.out.println(future.isDone());
    }

}

package org.abigtomato.nebula.thread.juc.t05;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal：
 * 为每个线程单独保存特有的变量
 * 底层是Map映射，key -> Thread.getCurrentThread() : value -> 线程需要保存的变量
 * ThreadLocal.set(value) -> map.put(Thread.getCurrentThread(), value);
 * ThreadLocal.get() -> map.get(Thread.getCurrentThread());
 *
 * 内存问题：
 * 在并发量高的时候，可能有内存溢出
 *
 * 注意：
 * 使用ThreadLocal的时候，一定注意回收资源问题，每个线程结束之前，将当前线程保存的线程变量一定要删除
 * ThreadLocal.remove();
 */
public class Test_01_ThreadLocal {

    volatile static String name = "hadoop";
    static ThreadLocal<String> tl = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + ":" + name);
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());

            tl.set("hive");
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());

            tl.remove();
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            name = "spark";
            tl.set("tensorflow");
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());

            tl.remove();
        }).start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":" + tl.get());
    }

}

package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CachedThreadPool -> 缓存型线程池
 */
public class Test_04_CachedThreadPool {

    public static void main(String[] args) {
        /**
         * CachedThreadPool -> 缓存型线程池，容量不限(Integer.MAX_VALUE)，自动扩容
         * 容量管理策略：
         *  1.如果线程池中的线程数量不满足任务的执行，创建新的线程
         *  2.每次有新任务无法即时处理的时候，会创建新的线程
         *  3.当线程池中的线程空闲时长达到一定的临界值(默认60秒)，自动释放内存资源
         * 应用场景：
         *  1.内部应用，有条件的内部数据瞬间处理时应用，如：电信平台夜间执行数据整理(有把握在短时间内处理完所有工作，且对硬件和软件有足够的信心)
         *  2.测试应用，在测试的时候尝试得到硬件或软件的最高负载量，用于提供FixedThreadPool容量的指导
         */
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println("无任务时线程池状态：" + service);

        for (int i = 0; i < 5; i++) {
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
        System.out.println("存在多个任务时线程池状态：" + service);

        try {
            TimeUnit.SECONDS.sleep(65);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("超过线程最大空闲时间后线程池的状态：" + service);
    }

}

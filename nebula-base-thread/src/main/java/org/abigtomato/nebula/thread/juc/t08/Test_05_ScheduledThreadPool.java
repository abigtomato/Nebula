package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledThreadPool -> 计划任务线程池
 */
public class Test_05_ScheduledThreadPool {

    public static void main(String[] args) {
        /**
         * ScheduledThreadPool -> 计划任务线程池，可以根据定时计划自动执行任务的线程池
         * 提供方法scheduleAtFixedRate(Runnable, start_limit, limit, timeunit)
         *  runnable - 要执行的任务
         *  start_limit - 第一次任务执行的时间间隔
         *  limit - 多次任务执行的时间间隔
         *  timeunit - 任务执行间隔的时间单位
         * 使用场景：
         *  计划任务时选用(DelaydQueue)
         *  如：电信行业中的数据整理，没分钟，小时，天整理等
         */
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        System.out.println("新实例的线程池状态：" + service);

        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务处理：" + Thread.currentThread().getName());
            }
        }, 0, 300, TimeUnit.MILLISECONDS);
    }

}

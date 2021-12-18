package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SingleThreadExecutor -> 单例线程池
 */
public class Test_06_SingleThreadExecutor {

    public static void main(String[] args) {
        /**
         * SingleThreadExecutor -> 单一容量的线程池
         * 使用场景：
         *  保证任务顺序执行时使用
         *  如：游戏大厅中的公共频道聊天，秒杀等业务
         */
        ExecutorService service = Executors.newSingleThreadExecutor();
        System.out.println(service);

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
    }

}

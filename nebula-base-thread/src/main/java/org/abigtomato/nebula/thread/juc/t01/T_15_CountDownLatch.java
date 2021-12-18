package org.abigtomato.nebula.thread.juc.t01;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch门闩机制：
 * 可以和锁混合使用，或替代锁的功能
 * 在门闩未完全开放之前等待，当门闩完全开放后执行
 * 避免锁效率低下的问题
 *
 * @author 16798
 */
public class T_15_CountDownLatch {

    /**
     * 为门闩设置5把锁
     */
    private final CountDownLatch latch = new CountDownLatch(5);

    void m1() {
        try {
            // 等待门闩上的锁全部解开
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m1 start");
    }

    void m2() {
        for (int i = 0; i < 10; ++i) {
            if (latch.getCount() != 0) {
                System.out.println("latch count:" + latch.getCount());
                // 释放，门闩的一把锁
                latch.countDown();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("m2() method:" + i);
        }
    }

    public static void main(String[] args) {
        final T_15_CountDownLatch t = new T_15_CountDownLatch();
        new Thread(t::m1).start();
        new Thread(t::m2).start();
    }
}

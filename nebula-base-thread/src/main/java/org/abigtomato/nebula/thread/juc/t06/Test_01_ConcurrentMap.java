package org.abigtomato.nebula.thread.juc.t06;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

/**
 * Hashtable
 * ConcurrentHashMap/ConcurrentHashSet
 * ConcurrentSkipListMap/ConcurrentSkipListSet
 */
public class Test_01_ConcurrentMap {

    public static void main(String[] args) {
        /**
         * 哈希表，内部线程安全
         */
        final Map<String, String> map = new Hashtable<>();
        /**
         * 底层哈希实现的同步Map/Set
         * 效率高，线程安全，使用系统底层技术实现线程安全
         * 量级较synchronized低，key和value不能为null
         */
        final Map<String, String> cmap = new ConcurrentHashMap<>();
        /**
         * 底层使用跳表(SkipList)实现的同步Map/Set
         * 有序，效率比ConcurrentHashMap稍低
         */
        final Map<String, String> csmap = new ConcurrentSkipListMap<>();

        final Random r = new Random();
        final Thread[] array = new Thread[100];
        final CountDownLatch latch = new CountDownLatch(array.length);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < array.length; i++) {
            array[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    map.put("key" + r.nextInt(100000), "value" + r.nextInt(100000));
                }
                latch.countDown();
            });
        }

        for (Thread thread : array) {
            thread.start();
        }

        try {
            /**
             * 主线程等待门闩开放
             */
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("执行时间为:" + (end - begin) + "毫秒!");
    }

}

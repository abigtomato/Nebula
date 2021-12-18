package org.abigtomato.nebula.thread.juc.t06;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Vector
 * CopyOnWriteArrayList
 */
public class Test_02_CopyOnWriteList {

    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();
        /**
         * Vector底层线程安全
         */
        final List<String> vector = new Vector<>();
        /**
         * 写时复制集合：
         * 写入效率低，读取效率高
         * 每次写入数据，都会拷贝一个新的底层数组
         * 每次读取数据，都会返回最后的数组，删除则同样移除最新的数组
         */
        final List<String> cowList = new CopyOnWriteArrayList<>();

        final Random random = new Random();
        final Thread[] array = new Thread[100];
        final CountDownLatch latch = new CountDownLatch(array.length);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < array.length; i++) {
            array[i] = new Thread(() -> {
                for (Thread thread : array) {
                    list.add("value" + random.nextInt(100000));
                }
                latch.countDown();
            });
        }

        for (Thread thread : array) {
            thread.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("执行时间为:" + (end-begin) + "毫秒!");
        System.out.println("List.size() : " + list.size());
    }

}

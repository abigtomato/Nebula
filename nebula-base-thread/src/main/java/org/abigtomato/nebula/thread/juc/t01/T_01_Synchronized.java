package org.abigtomato.nebula.thread.juc.t01;

/**
 * FileName: _01_Synchronized.java
 * Description: synchronized关键字
 * Copyright (C), 2019-2020 All Rights Reserved.
 * author:   zhy  createTime 2020/7/24 10:38
 * version:  1.0
 */
public class T_01_Synchronized {

    private int count = 0;
    private final Object object = new Object();

    /**
     * 对临界资源对象(可以被多个线性访问的对象)添加锁
     * <p>
     * 线程栈帧：
     * 若是基本数据类型，保存在栈帧中
     * 若是对象类型，栈帧中保存指向在堆中开辟了空间的对象
     * <p>
     * 加锁：
     * 第一个线程在对象上添加标记锁
     * 其他线程访问该对象时会检查锁，若存在锁则在锁池中等待锁的释放
     */
    public void testSync01() {
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + "count=" + count++);
        }
    }

    /**
     * 对当前对象添加锁1
     */
    public void testSync02() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + "count=" + count++);
        }
    }

    /**
     * 对当前对象添加锁2
     */
    public synchronized void testSync03() {
        System.out.println(Thread.currentThread().getName() + "count=" + count++);
    }
}

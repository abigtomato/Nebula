package org.abigtomato.nebula.thread.juc.t01;

/**
 * FileName: T_02_Synchronized.java
 * Description: synchronized static 方法
 * Copyright (C), 2019- All Rights Reserved.
 * author:   zhy  createTime 2020/7/24 10:39
 * version:  1.0
 */
public class T_02_Synchronized {

    private static int staticCount = 0;

    /**
     * 静态同步方法，锁当前类的class对象
     */
    public static synchronized void testSync04() {
        System.out.println(Thread.currentThread().getName() + "staticCount=" + staticCount++);
    }

    public static void testSync05() {
        synchronized (T_02_Synchronized.class) {
            System.out.println(Thread.currentThread().getName() + "staticCount=" + staticCount++);
        }
    }
}

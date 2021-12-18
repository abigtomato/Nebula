package org.abigtomato.nebula.thread.juc.t01;

import java.util.concurrent.TimeUnit;

/**
 * volatile关键字保证数据可见性
 * 通知os操作系统底层，在cpu计算过程中，都要检查内存中的数据有效性，保证最新的内存数据被使用
 */
public class T_09_Volatile {

    /**
     * 1.java代码通过jvm编译后的字节码文件存放在磁盘，执行时加载进内存，生成类的class对象和其他具体对象；
     * 2.对象的引用存在栈内存，对象内部属性存在堆内存，栈内存中的应用指向堆内存中的对象；
     * 3.cpu执行时会将属性加载进自己的高速缓存中，在一次不中断的计算中都只会读取缓存中的数据，若此时内存中的数据
     * 发生改变，cpu也不会去内存中同步；
     * 4.若对象属性声明了volatile关键字，cpu每次计算都会去内存中同步更新，避免缓存带来的问题。
     */
    volatile boolean flag = true;

    void m() {
        System.out.println("m start");
        while (flag) {
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        final T_09_Volatile t = new T_09_Volatile();

        new Thread(() -> t.m()).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.flag = false;
    }
}

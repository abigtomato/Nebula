package org.abigtomato.nebula.thread.juc.t01;

import java.util.concurrent.TimeUnit;

/**
 * 子类重写父类同步方法，锁可重入
 */
public class T_07_Synchronized {

    synchronized void m() {
        System.out.println("super m start");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("super m end");
    }

    public static void main(String[] args) {
        new Sub_Test_07().m();
    }

    static class Sub_Test_07 extends T_07_Synchronized {

        @Override
        synchronized void m() {
            System.out.println("sub m start");
            super.m();
            System.out.println("sub m end");
        }

    }

}


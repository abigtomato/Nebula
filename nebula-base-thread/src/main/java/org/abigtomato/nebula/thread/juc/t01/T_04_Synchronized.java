package org.abigtomato.nebula.thread.juc.t01;

/**
 * 同步方法只影响锁定同一个对象的同步方法
 * 不影响其他线程调用非同步方法，或调用其他锁资源的同步方法
 */
public class T_04_Synchronized {

    private final Object object = new Object();

    public synchronized void m1() {
        System.out.println("public synchronized void m1() start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("public synchronized void m1() end");
    }

    public void m2() {
        System.out.println("public void m2() start");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("public void m2() end");
    }

    public void m3() {
        synchronized (object) {
            System.out.println("public void m3() synchronized object start");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("public void m3() synchronized object end");
        }
    }

    private static class MyThread implements Runnable {

        private final int i;
        private final T_04_Synchronized t;

        public MyThread(int i, T_04_Synchronized t) {
            this.i = i;
            this.t = t;
        }

        @Override
        public void run() {
            if (i == 0) {
                t.m1();
            } else if (i > 0) {
                t.m2();
            } else {
                t.m3();
            }
        }

    }

    public static void main(String[] args) {
        T_04_Synchronized test_04 = new T_04_Synchronized();
        new Thread(new MyThread(0, test_04), "1").start();
        new Thread(new MyThread(1, test_04), "2").start();
        new Thread(new MyThread(-1, test_04), "3").start();
    }
}

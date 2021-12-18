package org.abigtomato.nebula.thread.juc.t01;

import java.util.concurrent.TimeUnit;

/**
 * 同步方法只能保证当前方法的原子性，不能保证多个业务方法访问同一个资源的原子性
 * 注意在商业开发中，多方法要求访问同一个资源，需要多个方法都加锁，且锁定同一个对象
 */
public class T_05_Synchronized {

    private double d;

    public synchronized void m1(double d) {
        try {
            // 睡眠的2秒相对于复杂的业务逻辑代码
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.d = d;
    }

    public double m2() {
        return this.d;
    }

    public static void main(String[] args) {
        final T_05_Synchronized t = new T_05_Synchronized();

        new Thread(() -> t.m1(100.0)).start();
        System.out.println(t.m2());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.m2());
    }
}

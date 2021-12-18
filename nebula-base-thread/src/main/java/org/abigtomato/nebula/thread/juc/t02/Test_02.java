package org.abigtomato.nebula.thread.juc.t02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 面试题(synchronized + 等待唤醒机制实现)：
 *  自定义容器，提供新增元素(add)和获取元素数量(size)方法
 *  启动两个线程。线程1向容器中新增10个数据。线程2监听容器元素数量，当容器元素数量为5时，线程2输出信息并终止。
 */
public class Test_02 {

    private static class MyContainer {

        volatile List<Integer> container = new ArrayList<>();

        public void add(Integer elem) {
            container.add(elem);
        }

        public int size() {
            return container.size();
        }

    }

    public static void main(String[] args) {
        final MyContainer mc = new MyContainer();
        final Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                for(int i=0; i<10; ++i) {
                    mc.add(i);
                    System.out.println(Thread.currentThread().getName() + " add:" + i);

                    if(mc.size() == 5) {
                        lock.notifyAll();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Thread-1").start();

        new Thread(() -> {
            synchronized (lock) {
                if(mc.size() != 5) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " size:" + mc.size());
                lock.notifyAll();
            }
        }, "Thread-2").start();
    }

}

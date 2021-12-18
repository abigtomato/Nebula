package org.abigtomato.nebula.thread.juc.t06;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue
 */
public class Test_06_DelayQueue {

    /**
     * DelayQueue 延时队列
     * 根据比较机制，实现自定义处理顺序的队列，常用于定时任务
     */
    static BlockingQueue<MyTask_06> queue = new DelayQueue<>();

    public static void main(String[] args) throws InterruptedException {
        long value = System.currentTimeMillis();

        MyTask_06 task1 = new MyTask_06(value + 2000);
        MyTask_06 task2 = new MyTask_06(value + 1000);
        MyTask_06 task3 = new MyTask_06(value + 3000);
        MyTask_06 task4 = new MyTask_06(value + 2500);
        MyTask_06 task5 = new MyTask_06(value + 1500);

        queue.put(task1);
        queue.put(task2);
        queue.put(task3);
        queue.put(task4);
        queue.put(task5);

        System.out.println("DelayQueue: " + queue);
        System.out.println("System.currentTimeMillis(): " + value);

        for(int i = 0; i < 5; i++){
            /**
             * 按照队列中元素的顺序出队
             */
            System.out.println(queue.take());
        }
    }

    static class MyTask_06 implements Delayed {

        private long compareValue;

        public MyTask_06(long compareValue) {
            this.compareValue = compareValue;
        }

        /**
         * 规定队列中元素比较性的方法
         * 根据参数TimeUnit来决定，如何返回结果值
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(compareValue - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * 自定义比较规则，自动实现升序
         * 建议和getDelay方法配合完成
         * 如果在DelayQueue是需要按时间完成的计划任务，必须配合getDelay方法完成
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            return (int)(this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "Task compare value is : " + this.compareValue;
        }

    }

}

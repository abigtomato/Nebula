package org.abigtomato.nebula.thread.juc.t08;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinPool -> 分支合并线程池，适合用于处理复杂任务
 * 1.线程池中运行的内容必须是ForkJoinTask的子类型(RecursiveTask，RecursiveAction)，其中提供了分支和合并的能力
 * RecursiveTask有返回结果的分支合并任务
 * RecursiveAction无返回结果的分支合并任务
 * 2.compute()方法(Callable/Runnable)就是任务的执行逻辑
 * 3.ForkJoinPool没有所谓的容量，默认都是1个线程，根据任务分支实例新的子线程
 * 4.当子线程任务结束后，自动合并，所谓自动是根据fork和join两个方法实现的
 * 5.应用：
 * 主要是做科学计算或天文计算，数据分析
 */
public class Test_07_ForkJoinPool {

    final static int[] NUMBERS = new int[1000000];
    final static int MAX_SIZE = 50000;
    final static Random RAN = new Random();

    static {
        for (int i = 0; i < NUMBERS.length; i++) {
            NUMBERS[i] = RAN.nextInt(1000);
        }
    }

    /**
     * 分支合并线程池的任务类
     */
    static class AddTask extends RecursiveTask<Long> {

        private static final long serialVersionUID = 1044448703128539606L;
        private final int begin;
        private final int end;

        public AddTask(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        /**
         * 任务执行的具体逻辑
         *
         * @return
         */
        @Override
        protected Long compute() {
            if ((end - begin) < MAX_SIZE) {
                long sum = 0L;
                for (int i = begin; i < end; i++) {
                    sum += NUMBERS[i];
                }
                return sum;
            } else {
                // 若容量达不到指定的范围，则继续递归调用拆分计算
                int middle = begin + (end - begin) / 2;

                AddTask task01 = new AddTask(begin, middle);
                AddTask task02 = new AddTask(middle, end);
                // fork()方法用于开启新的任务，相当于实例新的线程执行compute()内的逻辑
                task01.fork();
                task02.fork();

                // join()方法获取任务结束后的结果进行合并，阻塞式执行
                return task01.join() + task02.join();
            }
        }

    }

    public static void main(String[] args) {
        long result = 0L;
        for (int number : NUMBERS) {
            result += number;
        }
        System.out.println("单线程执行计算：" + result);

        try {
            ForkJoinPool pool = new ForkJoinPool();
            AddTask task = new AddTask(0, NUMBERS.length);
            Future<Long> future = pool.submit(task);
            System.out.println("多线程分支合并计算：" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

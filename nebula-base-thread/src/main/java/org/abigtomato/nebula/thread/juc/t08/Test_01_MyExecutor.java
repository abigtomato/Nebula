package org.abigtomato.nebula.thread.juc.t08;

import java.util.concurrent.Executor;

/**
 * Executor - 线程池底层处理机制
 * 在使用线程池的时候，底层如何调用线程中的逻辑
 */
public class Test_01_MyExecutor implements Executor {

    public static void main(String[] args)  {
        /**
         * 传入线程执行的逻辑
         */
        new Test_01_MyExecutor().execute(() -> System.out.println(Thread.currentThread().getName() + " - test executor"));
    }

    @Override
    public void execute(Runnable command) {
        /**
         * command相当于传入的线程，方法内部编写执行线程的逻辑
         */
        new Thread(command).start();
    }
}

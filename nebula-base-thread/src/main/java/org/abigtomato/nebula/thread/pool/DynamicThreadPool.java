package org.abigtomato.nebula.thread.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.abigtomato.nebula.thread.queue.ResizeLinkedBlockingQueue;

import java.util.concurrent.*;

/**
 * 动态线程池
 *
 * @author abigtomato
 */
public class DynamicThreadPool {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final ResizeLinkedBlockingQueue<Runnable> blockingQueue;

    private DynamicThreadPool() {
        // 默认线程池参数
        blockingQueue = new ResizeLinkedBlockingQueue<>(50);
        threadPoolExecutor = new ThreadPoolExecutor(10, 100,
                60, TimeUnit.SECONDS, blockingQueue,
                new ThreadFactoryBuilder().setNameFormat("common-thread-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ThreadPoolExecutor getOriginalThreadPool() {
        return threadPoolExecutor;
    }

    public void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    public Future<?> submit(Runnable task) {
        return threadPoolExecutor.submit(task);
    }

    public void shutDown() {
        threadPoolExecutor.shutdown();
    }

    public int getQueueCapacity() {
        return blockingQueue.getCapacity();
    }

    public static class Builder {

        private final DynamicThreadPool commonThreadPool;

        public Builder() {
            commonThreadPool = new DynamicThreadPool();
        }

        public Builder setCorePoolSize(int corePoolSize) {
            commonThreadPool.threadPoolExecutor.setCorePoolSize(corePoolSize);
            return this;
        }

        public Builder setMaximumPoolSize(int maximumPoolSize) {
            commonThreadPool.threadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
            return this;
        }

        public Builder setKeepAliveTime(long time, TimeUnit unit) {
            commonThreadPool.threadPoolExecutor.setKeepAliveTime(time, unit);
            return this;
        }

        public Builder setThreadFactory(ThreadFactory threadFactory) {
            commonThreadPool.threadPoolExecutor.setThreadFactory(threadFactory);
            return this;
        }

        public Builder setRejectedExecutionHandler(RejectedExecutionHandler handler) {
            commonThreadPool.threadPoolExecutor.setRejectedExecutionHandler(handler);
            return this;
        }

        public Builder allowCoreThreadTimeOut() {
            commonThreadPool.threadPoolExecutor.allowCoreThreadTimeOut(true);
            return this;
        }

        public Builder preStartCoreThread() {
            commonThreadPool.threadPoolExecutor.prestartAllCoreThreads();
            return this;
        }

        public Builder setQueueCapacity(int capacity) {
            commonThreadPool.blockingQueue.setCapacity(capacity);
            return this;
        }

        public DynamicThreadPool build() {
            commonThreadPool.submit(() -> {
//                for (;;) {
//
//                }
//                BigDecimal active = new BigDecimal(commonThreadPool.threadPoolExecutor.getActiveCount());
//                BigDecimal max = new BigDecimal(commonThreadPool.threadPoolExecutor.getMaximumPoolSize());
//                active.divide(max, RoundingMode.HALF_UP);
//                if (commonThreadPool.threadPoolExecutor.getActiveCount() / commonThreadPool.threadPoolExecutor.getMaximumPoolSize() >= 0.7) {
//                    //最小核心线程数和最大线程数的值，设置为相同最佳
//                    executor.setCorePoolSize(Double.valueOf(Math.round(executor.getCorePoolSize() * 1.5)).intValue());
//                    executor.setMaximumPoolSize(Double.valueOf(Math.round(executor.getMaximumPoolSize() * 1.5)).intValue());
//                    executor.prestartAllCoreThreads();
//                    reWritedCapacityLinkedBlockingQueue.setCapacity(Double.valueOf(Math.round(reWritedCapacityLinkedBlockingQueue.getCapacity() * 1.2)).intValue());
//                    TheCustomDynamicThreadPoolUtil.threadPoolStatus(executor, "线程池扩容之后");
//                }
//                if (executor.getActiveCount() / executor.getMaximumPoolSize() < 0.6) {
//                    //核心线程数是否大于2，如果大于2，判断活跃线程数扩大1.5倍后是否不为0（有可能在程序执行到此刻的时候，唯一一个执行任务的线程已经完成了任务，活跃线程就会为0）
//                    //如果核心线程数大于2为真，则核心线程数/2，或通过活跃线程数*1.5进行扩容（条件为，活跃线程数扩大1.5倍后是否为0
//                    //如果核心线程数大于2为假，那么直接设定线程数的最低值2  math.round 四舍五入取整
//                    executor.setCorePoolSize(executor.getCorePoolSize() > 2 ?
//                            (Double.valueOf(Math.round(executor.getActiveCount() * 1.5)).intValue() == 0 ? Double.valueOf(Math.round(executor.getCorePoolSize() / 2)).intValue() :
//                                    Double.valueOf(Math.round(executor.getActiveCount() * 1.5)).intValue()) : 2);executor.setMaximumPoolSize(executor.getMaximumPoolSize() > 2 ?
//                            (Double.valueOf(Math.round(executor.getActiveCount() * 1.5)).intValue() == 0 ?
//                                    Double.valueOf(Math.round(executor.getMaximumPoolSize() / 2)).intValue() :
//                                    Double.valueOf(Math.round(executor.getActiveCount() * 1.5)).intValue()) : 2);
//                    executor.prestartAllCoreThreads();
//                    //如果队列大于5，则进行-5后的结果判断，如果减5后大于等于5，那么执行减5操作，如果大于等于5不成立，则直接修改队列数量为5
//                    reWritedCapacityLinkedBlockingQueue.setCapacity(reWritedCapacityLinkedBlockingQueue.getCapacity() > 5 ?
//                            (Math.max(reWritedCapacityLinkedBlockingQueue.getCapacity() - 5, 5)) : 5);
//                    TheCustomDynamicThreadPoolUtil.threadPoolStatus(executor, "线程池收缩之后");
//                }
            });
            return commonThreadPool;
        }
    }
}
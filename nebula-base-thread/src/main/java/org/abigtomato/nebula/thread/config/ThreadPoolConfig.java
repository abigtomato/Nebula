package org.abigtomato.nebula.thread.config;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池参数
 *
 * @author abigtomato
 */
public class ThreadPoolConfig {

    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数量: 线程池维护线程的最少数量，即使没有任务需要执行，也会一直存活
        executor.setCorePoolSize(20);
        // 设置线程活跃时间（秒）: 允许的空闲时间，当线程空闲时间达到keepAliveTime时，线程会退出
        executor.setKeepAliveSeconds(50);
        // 线程池维护线程的最大数量: 当线程数>=corePoolSize，且任务队列已满时。线程池会创建新线程来处理任务
        // 当线程数=maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
        executor.setMaxPoolSize(20);
        // 设置队列容量: 缓存队列（阻塞队列）当核心线程数达到最大时，新任务会放在队列中排队等待执行
        executor.setQueueCapacity(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("op-");
        // 设置task的拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}

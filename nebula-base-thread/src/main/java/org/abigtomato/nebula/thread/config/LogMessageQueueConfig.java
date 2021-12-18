package org.abigtomato.nebula.thread.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yagushou
 * @date 2021-11-12
 */
@Data
@Configuration
public class LogMessageQueueConfig {

    private boolean open;

    /**
     * 队列缓冲区大小
     */
    @Value("${log.handler.disruptor.bufferSize:1024}")
    private int bufferSize;

    /**
     * 队列生产者线程数
     */
    @Value("${log.handler.disruptor.producerSize:1}")
    private int producerSize;

    /**
     * 队列消费者线程数
     */
    @Value("${log.handler.disruptor.workerSize:3}")
    private int workerSize;

    /**
     * 日志批次数量阈值
     */
    @Value("${log.handler.batch.sizeThreshold:1000}")
    private int sizeThreshold;

    /**
     * 日志批次超时时间
     */
    @Value("${log.handler.batch.timeout:1}")
    private long timeout;

    /**
     * 日志批次收集线程数
     */
    @Value("${log.handler.batch.collect.threadSize:3}")
    private int collectThreadSize;

    /**
     * 日志批次收集-协程调度池-线程数
     */
    @Value("${log.handler.fiber.scheduler.parallelism:5}")
    private int schedulerParallelism;

    /**
     * 日志批次收集-协程数
     */
    @Value("${log.handler.fiber.collect.fiberSize:500}")
    private int collectFiberSize;
}

package org.abigtomato.nebula.thread.disruptor;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.abigtomato.nebula.thread.config.LogMessageQueueConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class LogMessageQueue {

    private LogMessageQueueConfig logMessageQueueConfig;

    /**
     * 超时时间单位
     */
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    /**
     * 无锁并发队列
     */
    private Disruptor<LogMessageEvent> disruptor;

    /**
     * 日志批次事件生产者
     */
    private LogMessageEventProducer producer;

    /**
     * 用于提交日志批次的线程池
     */
    private ThreadPoolExecutor logBatchThreadPool;

    /**
     * 用于收集日志批次的阻塞队列
     */
    private BlockingQueue<Object> logBatchQueue;

    public void init() {
        if (!logMessageQueueConfig.isOpen()) {
            log.info("=====> [日志处理队列] 开关处于关闭状态, 跳过处理端初始化.");
            return;
        }
        log.info("=====> [日志处理队列] 开始初始化, 配置: {}", logMessageQueueConfig);

        // BlockingWaitStrategy阻塞等待策略、SleepingWaitStrategy睡眠等待策略、YieldingWaitStrategy让出等待策略
        disruptor = new Disruptor<>(
                new LogMessageEventFactory(),
                logMessageQueueConfig.getBufferSize(),
                new ThreadFactoryBuilder().setNameFormat("disruptor-thread-%d").build(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy());
        log.info("=====> [日志处理队列] disruptor队列初始化完成, 缓冲区大小: {}", logMessageQueueConfig.getBufferSize());

        WorkHandler<LogMessageEvent>[] consumers = new LogMessageWorkHandler[logMessageQueueConfig.getWorkerSize()];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new LogMessageWorkHandler();
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        log.info("=====> [日志处理队列] disruptor事件处理线程池初始化完成, 线程数: {}", logMessageQueueConfig.getWorkerSize());

        disruptor.start();

        RingBuffer<LogMessageEvent> ringBuffer = disruptor.getRingBuffer();
        producer = new LogMessageEventProducer(ringBuffer);
        log.info("=====> [日志处理队列] disruptor事件生产者初始化完成.");

        logBatchQueue = new ArrayBlockingQueue<>(logMessageQueueConfig.getSizeThreshold(), true);
        log.info("=====> [日志处理队列] 日志批次收集队列初始化完成, 队列容量: {}", logMessageQueueConfig.getSizeThreshold());

        logBatchThreadPool = new ThreadPoolExecutor(logMessageQueueConfig.getCollectThreadSize(),
                logMessageQueueConfig.getCollectThreadSize(), 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1),
                new BasicThreadFactory.Builder().namingPattern("logBatch-thread-%d").daemon(true).build());
        for (int i = 0; i < logMessageQueueConfig.getCollectThreadSize(); i++) {
            logBatchThreadPool.submit(() -> {
                for (; ; ) {
                    if (!logMessageQueueConfig.isOpen()) {
                        log.info("=====> 检测到开关关闭, 终止处理端.");
                        shutdown();
                        return;
                    }

                    // 定量处理日志批次, Queues.drain使队列按批次出队
                    List<Object> sysUsageIncrementDtoList =
                            Lists.newArrayListWithCapacity(logMessageQueueConfig.getSizeThreshold());
                    try {
                        Queues.drain(logBatchQueue, sysUsageIncrementDtoList,
                                logMessageQueueConfig.getSizeThreshold(),
                                logMessageQueueConfig.getTimeout(), TIME_UNIT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    producer.produce(sysUsageIncrementDtoList);
                }
            });
        }
        log.info("=====> [日志处理队列] 日志批次监控任务初始化完成, 线程数: {}", logMessageQueueConfig.getCollectThreadSize());
    }

    public void produce(Object obj) {
        logBatchQueue.add(obj);
    }

    public void shutdown() {
        shutdown(null);
    }

    public void shutdown(Long timeout) {
        if (timeout != null) {
            try {
                disruptor.shutdown(timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            disruptor.shutdown();
        }
        logBatchThreadPool.shutdown();
    }
}

package org.abigtomato.nebula.thread.disruptor.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRemarkRetryQueue {

    private final DelayQueue<OrderRemarkRetry> delayQueue = new DelayQueue<>();

    /**
     * 添加重试任务
     *
     * @param retry 重试任务
     * @return this
     */
    public OrderRemarkRetryQueue add(OrderRemarkRetry retry) {
        delayQueue.put(retry);
        return this;
    }

    /**
     * 执行队列重试逻辑
     *
     * @param time 间隔时间
     */
    public void retry(Long time) {
        if (delayQueue.size() == 0) {
            return;
        }
        while (true) {
            try {
                OrderRemarkRetry remarkRetry = delayQueue.take();
                // TODO 执行重试逻辑
                Boolean retryResult = false;
                if (!retryResult && remarkRetry.getCurrentRetryCount() != remarkRetry.getMaxRetryCount()) {
                    // 再次重试 (重试失败且未达最大重试次数)
                    remarkRetry.addFailCount();
                    remarkRetry.setTime((System.currentTimeMillis() / 1000) + time);
                    delayQueue.put(remarkRetry);
                } else if (!retryResult && remarkRetry.getCurrentRetryCount() == remarkRetry.getMaxRetryCount()) {
                    // 已过最大重试次数
                } else {
                    // 重试成功
                }
                if (delayQueue.size() == 0) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
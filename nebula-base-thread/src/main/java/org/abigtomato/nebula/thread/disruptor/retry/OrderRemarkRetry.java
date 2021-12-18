package org.abigtomato.nebula.thread.disruptor.retry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderRemarkRetry implements Delayed {

    /**
     * 业务参数
     */
    private String shopId;
    private String orderId;
    private String openId;
    private String activityId;
    private String remark;
    private String star;

    /**
     * 当前重试次数
     */
    private int currentRetryCount = 1;
    /**
     * 最大重试次数
     */
    private int maxRetryCount;
    /**
     * 过期时间
     */
    private long time;

    public OrderRemarkRetry(String shopId, String orderId, String openId, String activityId,
                            String remark, String star, int maxRetryCount, long time) {
        this.shopId = shopId;
        this.orderId = orderId;
        this.openId = openId;
        this.activityId = activityId;
        this.remark = remark;
        this.star = star;
        this.maxRetryCount = maxRetryCount;
        this.time = time;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.time - System.currentTimeMillis() / 1000, TimeUnit.SECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        long result = this.getDelay(TimeUnit.SECONDS) - delayed.getDelay(TimeUnit.SECONDS);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void addFailCount() {
        this.currentRetryCount++;
    }
}

package org.abigtomato.nebula.thread.disruptor;

import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
class LogMessageEventProducer {

    private final RingBuffer<LogMessageEvent> ringBuffer;

    public void produce(List<Object> sysUsageIncrementDtoList) {
        long sequence = ringBuffer.next();
        try {
            LogMessageEvent logMessageEvent = ringBuffer.get(sequence);
            logMessageEvent.setSysUsageIncrementDtoList(sysUsageIncrementDtoList);
        } finally {
            log.info("=====> 日志批次入队: 序列sequence = {}", sequence);
            ringBuffer.publish(sequence);
        }
    }
}

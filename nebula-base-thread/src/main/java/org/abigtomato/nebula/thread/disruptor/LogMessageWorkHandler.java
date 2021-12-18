package org.abigtomato.nebula.thread.disruptor;

import com.lmax.disruptor.WorkHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
class LogMessageWorkHandler implements WorkHandler<LogMessageEvent> {

    @Override
    public void onEvent(LogMessageEvent logMessageEvent) {
        List<Object> sysUsageIncrementDtoList = logMessageEvent.getSysUsageIncrementDtoList();
        log.info("=====> 日志批次出队, 异步入库数量: {}", sysUsageIncrementDtoList.size());
//        sysUsageIncrementService.handleBatch(sysUsageIncrementDtoList);
    }
}

package org.abigtomato.nebula.thread.disruptor;

import lombok.Data;

import java.util.List;

@Data
public class LogMessageEvent {

    private List<Object> sysUsageIncrementDtoList;
}

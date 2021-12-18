package org.abigtomato.nebula.thread.disruptor;

import com.lmax.disruptor.EventFactory;

public class LogMessageEventFactory implements EventFactory<LogMessageEvent> {

    @Override
    public LogMessageEvent newInstance() {
        return new LogMessageEvent();
    }
}

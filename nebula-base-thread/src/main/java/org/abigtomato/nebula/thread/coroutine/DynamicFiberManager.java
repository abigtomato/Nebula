package org.abigtomato.nebula.thread.coroutine;

import co.paralleluniverse.common.monitoring.MonitorType;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.futures.AsyncCompletionStage;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import com.google.common.util.concurrent.UncaughtExceptionHandlers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DynamicFiberManager {

    public void initFiber() {
        Channel<String> stringChannel = Channels.newChannel(10);
        new Fiber<Void>("fork-join-fiber", new FiberForkJoinScheduler("fork-join-thread", 10,
                UncaughtExceptionHandlers.systemExit(), MonitorType.METRICS, false), 32, () -> {
            try {
                waitNextLayer();
                stringChannel.send("233");
                stringChannel.receive();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void waitNextLayer() throws SuspendExecution, ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        AsyncCompletionStage.get(future);
    }
}

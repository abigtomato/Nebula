package org.abigtomato.nebula.netty.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.Proxy;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author abigtomato
 */
public class RpcClientManager {

    private static Channel channel = null;
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

    public static <T> T getProxyService(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass},
                (proxy, method, args) -> {
                    RpcRequestMessage message = new RpcRequestMessage();
                    getChannel().writeAndFlush(message);

                    Promise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
                    RpcResponseMessageHandler.PROMISES.put(1, promise);

//                    promise.addListener(future -> {
//                        if (promise.isSuccess()) {
//                            return promise.get();
//                        } else {
//                            throw new RuntimeException(promise.cause());
//                        }
//                    });

                    promise.await();
                    if (promise.isSuccess()) {
                        return promise.getNow();
                    } else {
                        throw new RuntimeException(promise.cause());
                    }
                });
    }

    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        LOCK.lock();
        try {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        } finally {
            LOCK.unlock();
        }
    }

    public static void initChannel() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channelFactory(NioSocketChannel::new);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(LOGGING_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 7000).channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

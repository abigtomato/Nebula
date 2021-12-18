package org.abigtomato.nebula.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author abigtomato
 */
public class NettyServer {

    public static void main(String[] args) {
        // 1.创建两个NIO事件循环组BossGroup和WorkerGroup（实际上是两个线程池）
        // 2.BossGroup处理连接请求，WorkerGroup处理客户端业务
        // 3.两个线程池都是无限循环
        // 4.BossGroup和WorkerGroup含有的NioEventLoop（线程）个数
        // 5.EventLoopGroup默认线程数：CPU核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap()
                    // 设置两个线程组
                    .group(bossGroup, workerGroup)
                    // 使用NioSocketChannel作为服务端的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列中的连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置连接状态保持活动
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给WorkerGroup的EventLoop对应的Pipeline设置Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给Pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 可以使用一个集合管理 SocketChannel，再推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            System.out.printf("客户SocketChannel hashcode: %s%n", ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap
                    // 绑定端口并启动服务器，立即返回ChannelFuture，异步操作
                    .bind(6668).sync()
                    // 给ChannelFuture注册监听器，处理异步操作的结果
                    .addListener((future) -> {
                        if (future.isSuccess()) {
                            System.out.println("端口6668绑定成功");
                        } else {
                            System.out.println("端口6668绑定失败");
                        }
                    })
                    // 对关闭通道的事件进行监听，异步操作
                    .channel().closeFuture().sync();
            channelFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

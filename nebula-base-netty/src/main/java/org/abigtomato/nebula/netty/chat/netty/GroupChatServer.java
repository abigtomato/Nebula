package org.abigtomato.nebula.netty.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.StringJoiner;

/**
 * 通讯室服务端
 *
 * @author abigtomato
 */
@Slf4j
public class GroupChatServer {

    public static void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            new ServerBootstrap().group(bossGroup, workerGroup)
                    .channelFactory(NioServerSocketChannel::new)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new SimpleChannelInboundHandler<String>() {

                                final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

                                @Override
                                public void handlerAdded(ChannelHandlerContext ctx) {
                                    // 连接建立
                                    Channel channel = ctx.channel();
                                    channelGroup.writeAndFlush(channel.remoteAddress());
                                    channelGroup.add(channel);
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    // channel处于活动状态
                                    log.info("-----> address: {}", ctx.channel().remoteAddress());
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) {
                                    // channel处于不活动状态
                                    log.info("-----> address: {}", ctx.channel().remoteAddress());
                                }

                                @Override
                                public void handlerRemoved(ChannelHandlerContext ctx) {
                                    // 连接关闭
                                    channelGroup.writeAndFlush(ctx.channel().remoteAddress());
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
                                    // 处理消息
                                    Channel currentChannel = channelHandlerContext.channel();
                                    channelGroup.forEach(channel -> {
                                        if (channel != currentChannel) {
                                            channel.writeAndFlush(channel.remoteAddress().toString() + ":" + msg);
                                        } else {
                                            channel.writeAndFlush(msg);
                                        }
                                    });
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    // 处理异常
                                    log.info("-----> error: {}", cause.getMessage());
                                    ctx.close();
                                }
                            });
                        }
                    }).bind(8080).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("-----> error: {}", e.getCause().getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        GroupChatServer.run();
    }
}

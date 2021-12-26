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

import java.text.SimpleDateFormat;

/**
 * @author abigtomato
 */
public class GroupChatServer {

    private final int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            new ServerBootstrap().group(bossGroup, workerGroup)
                    .channelFactory(NioServerSocketChannel::new)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new SimpleChannelInboundHandler<String>() {

                                final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

                                @Override
                                public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                                    // 连接建立
                                    Channel channel = ctx.channel();
                                    channelGroup.writeAndFlush(channel.remoteAddress());
                                    channelGroup.add(channel);
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // channel处于活动状态
                                    System.out.println("ctx = " + ctx.channel().remoteAddress());
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    // channel处于不活动状态
                                    System.out.println("ctx = " + ctx.channel().remoteAddress());
                                }

                                @Override
                                public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                                    // 连接关闭
                                    channelGroup.writeAndFlush(ctx.channel().remoteAddress());
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                    // 处理消息
                                    Channel currentChannel = channelHandlerContext.channel();
                                    channelGroup.forEach(channel -> {
                                        if (channel != currentChannel) {
                                            channel.writeAndFlush(channel.remoteAddress() + " : " + msg);
                                        } else {
                                            channel.writeAndFlush(msg);
                                        }
                                    });
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    // 处理异常
                                    System.out.println("cause = " + cause.getMessage());
                                    ctx.close();
                                }
                            });
                        }
                    }).bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new GroupChatServer(7000).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

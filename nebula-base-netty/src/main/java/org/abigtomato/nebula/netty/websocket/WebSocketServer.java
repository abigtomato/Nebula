package org.abigtomato.nebula.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author abigtomato
 */
@Slf4j
public class WebSocketServer {

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channelFactory(NioServerSocketChannel::new)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    // http协议编解码器
                                    .addLast(new HttpServerCodec())
                                    // 分块写入
                                    .addLast(new ChunkedWriteHandler())
                                    // http多段传输聚合
                                    .addLast(new HttpObjectAggregator(8192))
                                    // 将http协议升级为websocket协议
                                    .addLast(new WebSocketServerProtocolHandler("/sayHello"))
                                    // 处理TextWebSocketFrame即ws协议的帧结构（frame）
                                    .addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {

                                        @Override
                                        public void handlerAdded(ChannelHandlerContext ctx) {
                                            // 唯一id
                                            log.info("-----> longText: {}", ctx.channel().id().asLongText());
                                            // 非唯一id
                                            log.info("-----> shortText: {}", ctx.channel().id().asShortText());
                                        }

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                                                    TextWebSocketFrame textWebSocketFrame) {
                                            log.info("-----> text: {}", textWebSocketFrame.text());
                                            channelHandlerContext.channel()
                                                    .writeAndFlush(new TextWebSocketFrame(new Date().toString()));
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                            log.info("-----> error: {}", cause.getMessage());
                                            ctx.close();
                                        }
                                    });
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new WebSocketServer().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package org.abigtomato.nebula.netty.chat.nio;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.List;
import java.util.Set;

/**
 * @author abigtomato
 */
public class GroupChatServer {

    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 连接监听管道
     */
    private ServerSocketChannel listenChannel;
    /**
     * 端口
     */
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        System.out.printf("监听线程: %s%n", Thread.currentThread().getName());
        try {
            while (true) {
                if (Thread.interrupted()) {
                    return;
                }
                if (selector.selectNow() > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    List<SelectionKey> selectionKeyList = Lists.newArrayListWithCapacity(selectionKeys.size());
                    for (SelectionKey key : selectionKeys) {
                        if (key.isAcceptable()) {
                            // 处理连接事件
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.printf("%s上线%n", sc.getRemoteAddress());
                        } else if (key.isReadable()) {
                            // 读取客户端的数据
                            readData(key);
                        }
                        selectionKeyList.add(key);
                    }
                    selectionKeyList.forEach(selectionKeys::remove);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            if (channel.read(buffer) > 0) {
                String msg = new String(buffer.array());
                System.out.printf("form 客户端: %s%n", msg);
                // 向其它的客户端转发消息
                sendDataToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.printf("%s离线%n", channel.getRemoteAddress());
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void sendDataToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.printf("服务器转发数据给客户端线程: %s%n", Thread.currentThread().getName());
        for (SelectionKey key : selector.selectedKeys()) {
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}

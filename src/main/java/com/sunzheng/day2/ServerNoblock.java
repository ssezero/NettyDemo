package com.sunzheng.day2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.sunzheng.day1.ByteBufferUtil.debugRead;

@Slf4j(topic = "server.")
public class ServerNoblock {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//指定非阻塞模式
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //4. accept 建立与客户端连接，SockerChanel 用来与客户端之间通讯
            SocketChannel sc = ssc.accept(); //非阻塞模式下，这个地方不会阻塞，如果没有监听到客户端则返回null
            if (sc != null) {
                log.debug("connected...{}", sc);
                sc.configureBlocking(false); //启动非阻塞
                channels.add(sc);
            }
            //持续读取list中的channel来读取数据
             for (SocketChannel channel : channels) {
                 //5. 接受客户端的消息
                 int read = channel.read(buffer);//如果客户端只是建立的连接，channel没有读取到数据，则channel.read返回0
                 //确定channel读取到了数据才输出
                 if(read>0){
                     buffer.flip();
                     debugRead(buffer);
                     buffer.clear();
                     log.debug("after read....{}",channel);
                 }

             }

        }

    }
}

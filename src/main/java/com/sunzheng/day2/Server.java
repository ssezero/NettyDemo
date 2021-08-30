package com.sunzheng.day2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.sunzheng.day1.ByteBufferUtil.debugRead;

@Slf4j(topic = "server.")
public class Server {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer=ByteBuffer.allocate(16);

        ServerSocketChannel ssc=ServerSocketChannel.open();

        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3.连接集合
        List<SocketChannel> channels=new ArrayList<>();
         while (true){
             //4. accept 建立与客户端连接，SockerChanel 用来与客户端之间通讯
             log.debug("conecting....");
             SocketChannel sc=ssc.accept(); //进行阻塞，如果有客户端进来才会往下执行
             log.debug("connected...{}",sc);
             channels.add(sc);
             for (SocketChannel channel : channels) {
                 //5. 接受客户端的消息
                 log.debug("before read....{}",channel);
                 channel.read(buffer);      //进行阻塞，只有客户端写入数据才会往下执行
                 buffer.flip();
                 debugRead(buffer);
                 buffer.clear();
                 log.debug("after read....{}",channel);
             }

         }

    }
}

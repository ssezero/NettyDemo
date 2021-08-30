package com.sunzheng.day2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/*
* 客户端读取
* */
public class ClientRead {
    public static void main(String[] args) throws IOException {
        SocketChannel sc=SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));

        //3. 开始读取数据
        ByteBuffer buffer=ByteBuffer.allocate(1024*1024);
        int read=0;
        while(true){
            read += sc.read(buffer);
            System.out.println(read);
            buffer.clear();
        }
    }
}

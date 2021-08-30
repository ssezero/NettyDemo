package com.sunzheng.day2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
/*
* 1.创建ServerScoketChannel
* 2.设置非阻塞模式
* 3.创建一个Selector
* 4.
* */
public class ServerWrite {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector=Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, 0, null);
                    sckey.interestOps(SelectionKey.OP_READ);
                    //1.写入大量字符
                    StringBuilder sb=new StringBuilder();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append("a");
                    }
                    //2. 把3000000存到buffer上
                    ByteBuffer buffer= Charset.defaultCharset().encode(sb.toString());
                    //3.判断是否有内容
                    if(buffer.hasRemaining()){
                        //4. 关注可写事件
                        sckey.interestOps(sckey.interestOps()+SelectionKey.OP_WRITE);
                        // 5. 把剩余的buffer放到key的附件
                        sckey.attach(buffer);
                    }

                }else  if(key.isWritable()){
                    //1. 先通过key 拿到buffer和channel,然后写入
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.write(buffer);
                    //如果已经写没了，取消key上的附件，和关注写事件
                    if(!buffer.hasRemaining()){
                        key.attach(null);
                        key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);
                    }
                }
            }
        }

    }
}

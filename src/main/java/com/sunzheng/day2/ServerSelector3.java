package com.sunzheng.day2;

import com.sunzheng.day1.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 主要解决客户端发送消息过长超过buffer 16位的限制
 */
@Slf4j(topic = "server.")
public class ServerSelector3 {
    private static void split(ByteBuffer source) {

        source.flip();
        for (int i=0;i<source.limit();i++){
            if(source.get(i)=='\n'){
                int length=i+1-source.position();
                ByteBuffer buffer=ByteBuffer.allocate(length);
                for(int j=0;j<length;j++){
                    buffer.put(source.get());
                }
                ByteBufferUtil.debugAll(buffer);
            }
        }
        source.compact();

    }
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("注册的服务端KEY: {}", sscKey);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    log.debug("进入了key是客户创建连接");
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    //1.把buffer注册到key上,保证每个连接都是以一个独立的buffer
                    SelectionKey sckey = sc.register(selector, 0, buffer);
                    sckey.interestOps(SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    log.debug("进入key是read模式...");
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        //2 通过key的attachement拿到buffer
                        ByteBuffer  buffer =(ByteBuffer) key.attachment();
                        int read = channel.read(buffer); //客户端正常退出的时候会返回个-1

                        if(read==-1){
                            key.cancel();
                        }else
                        {
                           split(buffer);
                           if(buffer.position()==buffer.limit()){
                               ByteBuffer newBuffer=ByteBuffer.allocate(buffer.capacity()*2);
                               buffer.flip();
                               newBuffer.put(buffer);
                               key.attach(newBuffer);
                           }
                        }

                    } catch (Exception e){
                        //处理客户端异常退出
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }

    }
}

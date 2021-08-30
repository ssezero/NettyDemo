package com.sunzheng.day2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

import static com.sunzheng.day1.ByteBufferUtil.debugRead;

@Slf4j(topic = "server.")
public class ServerSelector2 {

    public static void main(String[] args) throws IOException {
        // 1. 创建 selector ,管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //2. 建立 selector 和channel 的联系（注册）
        // Selectionkey 将来事件发生后可以通过通过selectionKey来指定是什么事件(ops:0,代表什么都不关注)
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 指定sscKey 只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("注册的服务端KEY: {}", sscKey);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            //3 .select 方法,没有事件发生，线程阻塞，有事件，线程才会恢复
            // select 在事件未处理时候不会阻塞，事件发后，要么处理，要么取消
            selector.select();
            //4 处理事件,selectedKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                log.debug("key的事件类型是:{}", key);
                //移除key
                iter.remove();
                //判断的key的类型
                if (key.isAcceptable()) {
                    //连接类型的key 是accept
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, 0, null);
                    sckey.interestOps(SelectionKey.OP_READ);
                    log.debug("客户端进行建立的chanel是:{}", sc);

                } else if (key.isReadable()) {
                    //连接类型的key 是read
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        int read = channel.read(buffer); //客户端正常退出的时候会返回个-1
                        if(read==-1){
                            key.cancel();
                        }else
                        {
                            buffer.flip();
                            log.debug("读取到的数据是：{}", Charset.defaultCharset().decode(buffer));
//                            debugRead(buffer);
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

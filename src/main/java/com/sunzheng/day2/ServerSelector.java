package com.sunzheng.day2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.sunzheng.day1.ByteBufferUtil.debugRead;

@Slf4j(topic = "server.")
public class ServerSelector {
    public static void main(String[] args) throws IOException {
        // 1. 创建 selector ,管理多个channel
        Selector selector=Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        //2. 建立 selector 和channel 的联系（注册）
        // Selectionkey 将来事件发生后可以通过通过selectionKey来指定是什么事件(ops:0,代表什么都不关注)
        /*key 可以关注的事件类型
        * 1.accetp:   连接请求是触发
        * 2.connect： 客户端用连接建立触发
        * 3.read   ： 可读事件
        * 4. write  可写事件
         */
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 指定sscKey 只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key {}",sscKey);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            //3 .select 方法,没有事件发生，线程阻塞，有事件，线程才会恢复
            // select 在事件未处理时候不会阻塞，事件发后，要么处理，要么取消
            log.debug("38");
            selector.select();
            log.debug("39");
            //4 处理事件,selectedKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                log.debug("event key:{}",key);
                //1.消费这个key
//                ServerSocketChannel channel=(ServerSocketChannel) key.channel();
//                SocketChannel sc = channel.accept();
//                log.debug("{}",sc);
                //2 取消这个key
                key.cancel();
            }
        }

    }
}

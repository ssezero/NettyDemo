package com.sunzheng.mutithread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sunzheng.day1.ByteBufferUtil.debugAll;

@Slf4j(topic = "c.")
public class Demo2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bosskey = ssc.register(boss, 0, null);
        bosskey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //增加2个woker
        Woker [] wokers=new Woker[2];
        for (int i = 0; i < 2; i++) {
            wokers[i]=new Woker("woker"+i);
        }
//        Woker woker = new Woker("woker1");
        AtomicInteger count=new AtomicInteger(1);
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            log.debug("26");
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = serverChannel.accept();
                    log.debug("连接上了。。。。。。{}", sc.getRemoteAddress());
                    sc.configureBlocking(false);
                    wokers[count.getAndIncrement()%wokers.length].register(sc);


                }
            }
        }
    }

    static class Woker implements Runnable {
        private String name;
        private Thread thread;
        private Selector selector;
        private boolean isregister = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Woker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!isregister) {
                this.thread = new Thread(this, name);
                selector = Selector.open();
                this.thread.start();
                this.isregister = true;
            }
            //向队列添加任务

            selector.wakeup(); //唤醒selector
            sc.register(this.selector, SelectionKey.OP_READ, null);
        }

        @Override
        public void run() {
            while (true) {
                log.debug("1");
                try {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            log.debug("开始读取数据.....");
                            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            debugAll(byteBuffer);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

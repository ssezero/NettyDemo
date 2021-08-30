package com.sunzheng.day1;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static com.sunzheng.day1.ByteBufferUtil.debugAll;

@Slf4j
public class BufferDemo {
    public static void main(String[] args) {
        ByteBuffer buffer=ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});
        buffer.flip();
        buffer.get(new byte[3]);
        debugAll(buffer);

        buffer.rewind();
        System.out.println((char) buffer.get());
        debugAll(buffer);
        buffer.rewind();

        //mark()和reset是同时使用的
        System.out.println((char)buffer.get()); //a
        System.out.println((char)buffer.get()); //b
        buffer.mark(); //c 特别重要我要打个标记
        System.out.println((char)buffer.get()); //c
        System.out.println((char)buffer.get()); //d
        //因为上面的c开始特别终于我想要再重新读取一边从c
        buffer.reset();//postion 又回到了mark的位置
        System.out.println("----------------------");
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());

        // get() 方法不会修改postion位置


    }
}

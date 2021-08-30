package com.sunzheng.day1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static com.sunzheng.day1.ByteBufferUtil.debugAll;

@Slf4j(topic = "c.")
public class BufferReadDemo {
    public static void main(String[] args) {
        //----------String 转到buffer
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes());
        debugAll(buffer1);

        //默认切换到读模式，postition 为0
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer2);
        //默认切换到读模式，postition 为0
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer3);

        //-----------Buffer 转到 String
        //因为buffer2是默认到读模式，所以可以输出
        String buffer2_str = StandardCharsets.UTF_8.decode(buffer2).toString();
        log.debug(buffer2_str);
        //因为buffer1是写默认，所以这个时候出不来值
        String buffer1_str = StandardCharsets.UTF_8.decode(buffer1).toString();
        log.debug(buffer1_str);

    }
}

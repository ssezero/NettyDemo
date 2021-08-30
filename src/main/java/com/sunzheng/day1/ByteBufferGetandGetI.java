package com.sunzheng.day1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j(topic = "c.demo")
public class ByteBufferGetandGetI {
    public static void main(String[] args) {
        ByteBuffer source=ByteBuffer.allocate(20);
        source.put("sunzheng".getBytes());
        source.flip();
        byte b = source.get(3);
        ByteBufferUtil.debugAll(source);
        log.debug("数据{}",b);

        byte b1 = source.get();
        ByteBufferUtil.debugAll(source);
        log.debug("数据{}",b1);

    }
}

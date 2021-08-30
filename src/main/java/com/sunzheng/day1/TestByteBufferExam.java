package com.sunzheng.day1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j(topic = "c.demo")
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source=ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

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
}

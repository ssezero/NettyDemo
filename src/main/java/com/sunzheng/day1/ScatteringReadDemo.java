package com.sunzheng.day1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.RandomAccess;

/**
 * 分散读
 * 3parts.txt 里面是 onetwothree 想3个单词读取到3个buffer里面采用
 * 分散读的方式
 */
public class ScatteringReadDemo {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("3parts.txt", "r")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer1 = ByteBuffer.allocate(3);
            ByteBuffer buffer2 = ByteBuffer.allocate(3);
            ByteBuffer buffer3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{buffer1,buffer2,buffer3});
            buffer1.flip();
            buffer2.flip();
            buffer3.flip();
            ByteBufferUtil.debugAll(buffer1);
            ByteBufferUtil.debugAll(buffer2);
            ByteBufferUtil.debugAll(buffer3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

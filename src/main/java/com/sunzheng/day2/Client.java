package com.sunzheng.day2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        SocketAddress localAddress = sc.getLocalAddress();
        sc.write(Charset.defaultCharset().encode("0123456789abcdef33330123456789abcdef33330123456789abcdef3333\n"));
        System.in.read();
    }
}
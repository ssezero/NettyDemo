package com.sunzheng;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Demo1 {
    static class OOMObject{

    }
    @Test
    public void  test1() {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);
    }
}

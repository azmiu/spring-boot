package com.azmiu;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class DThread implements Callable<Integer> {

    private Map<String, String> map;
    private CountDownLatch countDown;

    public DThread(Map<String, String> map, CountDownLatch countDown) {
        this.map = map;
        this.countDown = countDown;
    }

    @Override
    public Integer call() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}

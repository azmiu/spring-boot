package com.azmiu;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DThread implements Runnable {

    private Map<String, String> map;
    private CountDownLatch countDown;

    public DThread(Map<String, String> map, CountDownLatch countDown) {
        this.map = map;
        this.countDown = countDown;
    }

    @Override
    public void run() {
        this.delete(this.map);
    }

    public void delete(Map<String, String> map) {
        int count = 0;
        for (int i = 0; i < 1000000000; i++) {
            count++;
        }
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("呵呵哒" + count);
        this.countDown.countDown();
    }
}

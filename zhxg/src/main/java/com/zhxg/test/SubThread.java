package com.zhxg.test;


public class SubThread implements Runnable {

    private int i;
    public SubThread(int i) {
        this.i = i;
    }

    @Override
    public void run() {

        System.out.println(this.i);

    }

}

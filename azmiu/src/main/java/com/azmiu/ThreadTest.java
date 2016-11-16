package com.azmiu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.azmiu.utils.ExecutorProcessPool;

public class ThreadTest {
	
	
    private static AtomicInteger count = new AtomicInteger();
    private static List<Future<Integer>> futurelist = new ArrayList<Future<Integer>>();
    private static List<Integer> userList = new ArrayList<Integer>();

    public static void main(String args[]) throws InterruptedException, ExecutionException {

        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();


        // 获取用户列表
        for (int i = 1; i < 5; i++) {
            userList.add(i);
        }
        count.set(userList.size());
        while (checkStatus()) {
            count.set(0);
            System.out.println("-----初始化数据------");

            for (Integer id : userList) {
                Future<Integer> futrueI = pool.submit(new FeedBackCallable(count));
                System.out.println("++++++" + Thread.currentThread() + "---" + id);
                futurelist.add(futrueI);
            }
        }
    }

    public static boolean checkStatus() {
        boolean status = true;
        if (userList.size() == count.get()) {
            status = true;
        }
        for (Future<Integer> futrue : futurelist) {
            try {
                if (futrue.get() > 0) {
                    System.out.println(futrue.get());
                    status = true;
                    break;
                }else{
                    status = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
}

class FeedBackCallable implements Callable<Integer> {

    AtomicInteger tld = null;

    public FeedBackCallable(AtomicInteger val) {
        this.tld = val;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "++++++++++" + this.tld);
        this.tld.getAndIncrement();
        if(Thread.currentThread().getName().indexOf("3")>0){
            return 3;
        }else{
            return 0;
        }
    }
	
}



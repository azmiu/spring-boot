package com.azmiu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.azmiu.utils.ExecutorProcessPool;

@Component
public class Timer {

    private CountDownLatch countDown = new CountDownLatch(10);

    private static AtomicInteger count = new AtomicInteger(0);
    List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
    List<Future<?>> futurelist = new ArrayList<Future<?>>();

    // 17.52执行一次
    @Scheduled(cron = "0 28 10 ? * *")
    // 每隔5秒钟执行一次
    // @Scheduled(fixedRate = 1000)
    // @Scheduled(fixedDelay = 600000)
    // 12点到3点，每隔10分钟执行一次
    // @Scheduled(cron = "0 0/20 9-12 * * ?")
    public void test() {
        for (int i = 0; i < 5; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "" + i);
            map.put("name", "name" + i);
            this.userList.add(map);
        }
        while (this.userList.size() != this.futurelist.size()) {

            System.out.println("测试开始");
            // this.futurelist = new ArrayList<Future<?>>();
            // 时间在12点到3点之间，所有线程执行完毕，所有用户剩余数据量不为0
            if (this.userList.size() != this.futurelist.size()) {
                this.startWork();
            }
        }

    }

    public boolean checkStatus() {
        if (this.userList.size() != this.futurelist.size()) {
            return true;
        } else if ((this.userList.size() == this.futurelist.size())) {
            
        }
        return false;
    }

    public boolean checkFuture(){
        for (int i = 0; i < this.futurelist.size(); i++) {
            Future<?> future = this.futurelist.get(i);
            try {
                System.out.println("这里是在校验结果：" + future.get());
                System.out.println("这里是在看状态：" + future.isDone());
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void startWork() {
        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

        for (int i = 0; i < this.userList.size(); i++) {
            Future<?> future = pool.submit(new FeedBackCallable1());
            try {
                System.out.println(i + "用户线程执行状态：" + future.isDone() + "执行结果：" + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            this.futurelist.add(future);
        }
        System.out.println("结果list长度" + this.futurelist.size());
        System.out.println("所有人都做完了");
    }

    class FeedBackCallable1 implements Callable<Integer> {

        AtomicInteger tld = null;


        @Override
        public Integer call() {

            System.out.println("这里做一些业务逻辑，" + Thread.currentThread().getName() + "++++++++++" + this.tld);
            if (Thread.currentThread().getName().indexOf("3") > 0) {
                System.out.println("这个表数据比较多");
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 3;
            } else {
                return 0;
            }
        }

    }
}

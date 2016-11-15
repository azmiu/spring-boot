package com.azmiu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.azmiu.utils.ExecutorProcessPool;

@Component
public class Timer {

    private CountDownLatch countDown = new CountDownLatch(10);

    private int future = 0;
    List<Map<String, String>> userList = new ArrayList<Map<String, String>>();

    // 17.52执行一次
    @Scheduled(cron = "0 24 00 ? * *")
    // 每隔5秒钟执行一次
    // @Scheduled(fixedRate = 1000)
    // @Scheduled(fixedDelay = 600000)
    // 12点到3点，每隔10分钟执行一次
    // @Scheduled(cron = "0 0/20 9-12 * * ?")
    public void test() {
        System.out.println("测试开始");
        for (int i = 0; i < 5; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "" + i);
            map.put("name", "name" + i);
            this.userList.add(map);
        }
        while (this.userList.size() != this.future) {
            this.startWork();
        }
        System.out.println("所有人都做完了");
    }

    public void startWork() {
        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

        for (int i = 0; i < this.userList.size(); i++) {
            Future<?> future = pool.submit(new DThread(this.userList.get(i), this.countDown));
            try {
                if (null == future.get()) {
                    this.future++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

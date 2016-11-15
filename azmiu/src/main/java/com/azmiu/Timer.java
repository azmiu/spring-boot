package com.azmiu;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Timer {

    // 17.52执行一次
    // @Scheduled(cron = "0 49 14 ? * *")
    // 每隔5秒钟执行一次
    @Scheduled(fixedRate = 1000)
    // @Scheduled(fixedDelay = 600000)
    // 12点到3点，每隔10分钟执行一次
    // @Scheduled(cron = "0 0/20 9-12 * * ?")
    public void test() {
        System.out.println("测绘");
    }
}

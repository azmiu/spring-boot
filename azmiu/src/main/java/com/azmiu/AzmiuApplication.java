package com.azmiu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AzmiuApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzmiuApplication.class, args);
    }
}

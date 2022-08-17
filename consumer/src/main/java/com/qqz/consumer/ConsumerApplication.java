package com.qqz.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qqz @Date:2022/8/17
 */

@SpringBootApplication
@ComponentScan(basePackages = {"com.qqz.spring.annotation","com.qqz.spring.service","com.qqz.consumer.controller"})
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class,args);
    }
}

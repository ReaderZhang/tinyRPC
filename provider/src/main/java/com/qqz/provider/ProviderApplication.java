package com.qqz.provider;

import com.qqz.protocol.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qizhang.qiu
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.qqz.spring.annotation","com.qqz.spring.service","com.qqz.provider.service.dubbo"})
public class ProviderApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProviderApplication.class,args);
    }
}

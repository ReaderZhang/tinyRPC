package com.qqz.provider;

import com.qqz.protocol.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qizhang.qiu
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.qqz.spring"})
public class ProviderApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProviderApplication.class,args);
        new NettyServer("127.0.0.1",8888).startNettyServer();
    }
}

package com.qqz.spring.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * @author qqz @Date:2022/8/17
 */

@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcProviderAutoConfiguration {

    @Bean
    public SpringRpcProviderBean rpcProviderBean(RpcServerProperties rpcServerProperties) throws UnknownHostException {
        return new SpringRpcProviderBean(rpcServerProperties.getServicePort());
    }
}

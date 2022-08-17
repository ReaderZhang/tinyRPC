package com.qqz.spring.service;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author qqz @Date:2022/8/17
 */
@Configuration
public class RpcReferenceAutoConfiguration implements EnvironmentAware {


    @Bean
    public SpringRpcReferencePostProcessor postProcessor(){
        String address = environment.getProperty("tiny.serviceAddress");
        int port = Integer.parseInt(environment.getProperty("tiny.servicePort"));
        RpcClientProperties rc = new RpcClientProperties();
        rc.setServiceAddress(address);
        rc.setServicePort(port);
        return new SpringRpcReferencePostProcessor(rc);
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

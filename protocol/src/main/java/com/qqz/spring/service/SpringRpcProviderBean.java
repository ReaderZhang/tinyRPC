package com.qqz.spring.service;

import com.qqz.protocol.NettyServer;
import com.qqz.spring.annotation.TinyRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author qqz @Date:2022/8/17
 */

@Slf4j
public class SpringRpcProviderBean implements InitializingBean, BeanPostProcessor {

    private final int serverPort;
    private final String serverAddress;

    public SpringRpcProviderBean(int serverPort) throws UnknownHostException {
        this.serverPort = serverPort;
        InetAddress address = InetAddress.getLocalHost();
        this.serverAddress = address.getHostAddress();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("begin deploy Netty server to host{},on port {}",this.serverAddress,this.serverPort);
        new Thread(()->{
            try{
                new NettyServer(this.serverAddress,this.serverPort).startNettyServer();
            } catch (Exception e) {
                log.error("Start Netty Server Occur Exception,",e);
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //针对该注解进行特殊配置
        if (bean.getClass().isAnnotationPresent(TinyRemoteService.class)){
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method:methods){
                String key = bean.getClass().getInterfaces()[0].getName()+"."+method.getName();
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.beanMethodMap.put(key,beanMethod);
            }
        }
        return bean;
    }
}

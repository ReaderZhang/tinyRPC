package com.qqz.spring.service;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author qqz @Date:2022/8/17
 */
public class SpringRpcReferenceBean implements FactoryBean<Object> {

    private Class<?> interfaceClass;

    private Object object;

    private String serviceAddress;

    private int servicePort;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

//    public void init(){
//        this.object = Proxy.newProxyInstance(this.interfaceClass.getClassLoader(),
//                new Class<?>[]{this.interfaceClass},
//                new RpcInvokerProxy(this.serviceAddress,this.servicePort));
//    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }
}

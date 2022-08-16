package com.qqz.consumer.proxy;

import java.lang.reflect.Proxy;

/**
 * @author qizhang.qiu
 */
public class RpcClientProxy {
    public <T> T clientProxy(final Class<T> interfaceCls,final String host,final int port){
        return (T) Proxy.newProxyInstance(
                interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls},
                new RpcInvokerProxy(host,port)
        );
    }
}

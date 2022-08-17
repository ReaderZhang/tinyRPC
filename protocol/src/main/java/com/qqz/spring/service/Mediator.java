package com.qqz.spring.service;

import com.qqz.core.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qqz @Date:2022/8/17
 */
public class Mediator {

    public static Map<String,BeanMethod> beanMethodMap = new ConcurrentHashMap<>();

    private volatile static Mediator instance = null;

    private Mediator(){}

    public static Mediator getInstance(){
        if (instance == null){
            synchronized (Mediator.class){
                if (instance == null){
                    instance = new Mediator();
                }
            }
        }
        return instance;
    }

    public Object processor(RpcRequest rpcRequest){
        String key = rpcRequest.getClassName()+"."+rpcRequest.getMethodName();
        BeanMethod beanMethod = beanMethodMap.get(key);
        if (beanMethod == null){
            return null;
        }

        Object bean = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        try {
            return method.invoke(bean,rpcRequest.getParams());
        }catch (InvocationTargetException | IllegalAccessException ex){
            ex.printStackTrace();
        }
        return null;
    }
}

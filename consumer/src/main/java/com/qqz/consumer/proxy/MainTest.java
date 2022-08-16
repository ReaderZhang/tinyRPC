package com.qqz.consumer.proxy;

import com.qqz.api.IUserService;

/**
 * @author qizhang.qiu
 */
public class MainTest {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        IUserService userService = rpcClientProxy.clientProxy(IUserService.class,"localhost",20880);
        System.out.println(userService.saveUser("qqza"));
    }
}

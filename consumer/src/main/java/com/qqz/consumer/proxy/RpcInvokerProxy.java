package com.qqz.consumer.proxy;

import com.qqz.constants.ReqType;
import com.qqz.constants.RpcContant;
import com.qqz.constants.SerialType;
import com.qqz.core.*;
import com.qqz.protocol.NettyClient;
import com.qqz.protocol.NettyServer;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author qizhang.qiu
 */
@Slf4j
public class RpcInvokerProxy implements InvocationHandler {

    private String serviceAddress;

    private int servicePort;


    public RpcInvokerProxy(String serviceAddress, int servicePort) {
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("begin invoke target server");

        //组装参数
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
        Header header = new Header(RpcContant.MAGIC, SerialType.JSON_SERIAL.getCode(), ReqType.REQUEST.getCode(), requestId,0);
        protocol.setHeader(header);
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setContent(request);

        //发送请求
        NettyClient nettyClient = new NettyClient(serviceAddress,servicePort);

        //构建异步数据处理
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
        RequestHolder.REQUEST_MAP.put(requestId,future);
        nettyClient.sendRequest(protocol);

        return future.getPromise().get().getData();
    }
}

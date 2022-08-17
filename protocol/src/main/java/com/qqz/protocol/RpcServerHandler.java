package com.qqz.protocol;

import com.qqz.constants.ReqType;
import com.qqz.core.Header;
import com.qqz.core.RpcProtocol;
import com.qqz.core.RpcRequest;
import com.qqz.core.RpcResponse;
import com.qqz.spring.SpringBeansManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author qizhang.qiu
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        RpcProtocol resProtocol = new RpcProtocol();
        Header header = msg.getHeader();
        header.setReqType(ReqType.RESPONSE.getCode());
        Object result = invoke(msg.getContent());
        resProtocol.setHeader(header);
        RpcResponse response = new RpcResponse();
        response.setData(result);
        response.setMsg("success");
        resProtocol.setContent(response);

        ctx.writeAndFlush(resProtocol);
    }

    private Object invoke(RpcRequest request){
        try{
            Class<?> clazz = Class.forName(request.getClassName());
            Object bean = SpringBeansManager.getBean(clazz);
            Method declaredMethod = clazz.getDeclaredMethod(request.getMethodName(),request.getParameterTypes());
            return declaredMethod.invoke(bean,request.getParams());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

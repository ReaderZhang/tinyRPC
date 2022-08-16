package com.qqz.protocol;

import com.qqz.core.RequestHolder;
import com.qqz.core.RpcFuture;
import com.qqz.core.RpcProtocol;
import com.qqz.core.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qizhang.qiu
 */

@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        log.info("receive rpc server result");
        long requestId = msg.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getContent());
    }
}

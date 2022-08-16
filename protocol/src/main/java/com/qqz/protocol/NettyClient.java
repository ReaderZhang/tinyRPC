package com.qqz.protocol;

import com.qqz.core.RpcProtocol;
import com.qqz.core.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qizhang.qiu
 */

@Slf4j
public class NettyClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventExecutors = new NioEventLoopGroup();
    private String serviceAddress;
    private int servicePort;

    public NettyClient(String serviceAddress, int servicePort) {
        log.info("begin init NettyClient");
        bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioServerSocketChannel.class)
                .handler(new RpcClientInitializer());
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol) throws InterruptedException{
        ChannelFuture future = bootstrap.bind(this.serviceAddress,this.servicePort).sync();
        future.addListener(listener ->{
            if (future.isSuccess()){
                log.info("connect rpc server {} success",this.serviceAddress);
            }else{
                log.error("connect rpc server {} failed",this.serviceAddress);
                future.cause().printStackTrace();
                eventExecutors.shutdownGracefully();
            }
        });
        log.info("begin transfer data");
        future.channel().writeAndFlush(protocol);
    }


}

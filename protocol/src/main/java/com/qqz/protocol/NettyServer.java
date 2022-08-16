package com.qqz.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qizhang.qiu
 */

@Slf4j
public class NettyServer {

    /**
     * 访问地址
     */
    private String serverAddress;

    /**
     * 端口
     */
    private int port;

    public NettyServer(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void startNettyServer() throws Exception{
        log.info("begin start netty server");
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer());
            ChannelFuture channelFuture = bootstrap.bind(this.serverAddress,this.port);
            log.info("Server started success on port:{}",this.port);
            channelFuture.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

package com.qqz.codec;

import com.qqz.core.Header;
import com.qqz.core.RpcProtocol;
import com.qqz.serial.ISerializer;
import com.qqz.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author qizhang.qiu
 */

@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {

    /*
    +----------------------------------------------+
    | 魔数 2byte | 序列化算法 1byte | 请求类型 1byte  |
    +----------------------------------------------+
    | 消息 ID 8byte     |      数据长度 4byte       |
    +----------------------------------------------+
    */

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        log.info("==========Begin Decode ==========");
        Header header = msg.getHeader();

        //写入header各项数据
        out.writeShort(header.getMagic());
        out.writeByte(header.getSerialType());
        out.writeByte(header.getReqType());
        out.writeLong(header.getRequestId());

        //对数据进行序列化
        ISerializer serializer = SerializerManager.getSerializer(header.getSerialType());
        byte[] data = serializer.serialize(msg.getContent());
        header.setLength(data.length);
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}

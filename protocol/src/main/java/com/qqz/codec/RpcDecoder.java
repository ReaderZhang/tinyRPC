package com.qqz.codec;

import com.qqz.constants.ReqType;
import com.qqz.constants.RpcContant;
import com.qqz.core.Header;
import com.qqz.core.RpcProtocol;
import com.qqz.core.RpcRequest;
import com.qqz.core.RpcResponse;
import com.qqz.serial.ISerializer;
import com.qqz.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.Headers;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author qizhang.qiu
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("==========Begin Decode ==========");
        if (byteBuf.readableBytes() < RpcContant.HEAD_TOTAL_LEN) {
            //信息不够长，不需要解析
            return;
        }
        //标记读索引，后续用来重置
        byteBuf.markReaderIndex();

        //读取magic
        short magic = byteBuf.readShort();
        if (magic != RpcContant.MAGIC) {
            throw new IllegalArgumentException("Illegal request parameter 'magic'," + magic);
        }

        //读取序列化算法类型
        byte serialType = byteBuf.readByte();

        //请求类型
        byte reqType = byteBuf.readByte();

        //请求消息id
        long requestId = byteBuf.readLong();

        //请求消息长度
        int dataLen = byteBuf.readInt();
        //可读区域字节数少于实际数据长度
        if (byteBuf.readableBytes() < dataLen) {
            byteBuf.resetReaderIndex();
            return;
        }

        //获取消息内容
        byte[] content = new byte[dataLen];
        byteBuf.readBytes(content);

        //构造Header
        Header header = new Header(magic, serialType, reqType, requestId, dataLen);
        ISerializer serializer = SerializerManager.getSerializer(serialType);
        ReqType rt = ReqType.findByCode(reqType);
        switch (rt) {
            case REQUEST:
                RpcRequest request = serializer.deserialze(content, RpcRequest.class);
                RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
                reqProtocol.setHeader(header);
                reqProtocol.setContent(request);
                list.add(reqProtocol);
                break;
            case RESPONSE:
                RpcResponse response = serializer.deserialze(content, RpcResponse.class);
                RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
                resProtocol.setHeader(header);
                resProtocol.setContent(response);
                list.add(resProtocol);
                break;
            case HEARTBEAT:
                break;
            default:
                break;

        }
    }
}

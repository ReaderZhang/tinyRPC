package com.qqz.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Header implements Serializable {
    /*
    +----------------------------------------------+
    | 魔数 2byte | 序列化算法 1byte | 请求类型 1byte  |
    +----------------------------------------------+
    | 消息 ID 8byte     |      数据长度 4byte       |
    +----------------------------------------------+
    */

    //验证报文身份
    private short magic;

    //序列化类型
    private byte serialType;

    //操作类型
    private byte reqType;

    //请求id
    private long requestId;

    //数据长度
    private int length;
}

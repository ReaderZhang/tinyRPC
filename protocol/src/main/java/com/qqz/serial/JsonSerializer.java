package com.qqz.serial;

import com.alibaba.fastjson.JSON;
import com.qqz.constants.SerialType;

public class JsonSerializer implements ISerializer{
    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialze(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }

    @Override
    public byte getType() {
        return SerialType.JSON_SERIAL.getCode();
    }
}

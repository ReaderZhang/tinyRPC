package com.qqz.serial;

public interface ISerializer {
    <T> byte[] serialize(T obj);

    <T> T deserialze(byte[] data,Class<T> clazz);

    byte getType();
}

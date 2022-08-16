package com.qqz.constants;

public enum SerialType {
    JSON_SERIAL((byte)0),
    JAVA_SERIAL((byte)1),
    MESSAGEPACK_SERIAL((byte)2),
    PROTOBUF_SERIAL((byte)3);

    private byte code;

    SerialType(byte code){
        this.code = code;
    }

    public byte getCode(){
        return this.code;
    }
}

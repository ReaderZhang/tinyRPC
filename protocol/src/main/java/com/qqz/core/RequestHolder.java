package com.qqz.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qizhang.qiu
 */
public class RequestHolder {

    public static final AtomicLong REQUEST_ID = new AtomicLong();

    public static final Map<Long,RpcFuture> REQUEST_MAP = new ConcurrentHashMap<>();
}

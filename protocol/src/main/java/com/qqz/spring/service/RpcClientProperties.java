package com.qqz.spring.service;

import lombok.Data;

/**
 * @author qqz @Date:2022/8/17
 */
@Data
public class RpcClientProperties {
    private String serviceAddress = "127.0.0.1";

    private int servicePort = 20880;
}

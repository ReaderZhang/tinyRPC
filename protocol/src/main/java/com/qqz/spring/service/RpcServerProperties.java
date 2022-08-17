package com.qqz.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qqz @Date:2022/8/17
 */
@Data
@ConfigurationProperties(prefix = "tiny.rpc")
public class RpcServerProperties {

    private int servicePort;
}

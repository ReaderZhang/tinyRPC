package com.qqz.spring.service;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author qqz @Date:2022/8/17
 */

@Data
public class BeanMethod {

    private Object bean;

    private Method method;
}

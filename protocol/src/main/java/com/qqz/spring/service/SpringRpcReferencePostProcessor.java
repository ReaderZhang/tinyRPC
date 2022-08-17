package com.qqz.spring.service;

import com.qqz.constants.RpcContant;
import com.qqz.spring.annotation.TinyRemoteReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qqz @Date:2022/8/17
 */
@Slf4j
public class SpringRpcReferencePostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ApplicationContext context;

    private ClassLoader classLoader;

    private RpcClientProperties clientProperties;


    public SpringRpcReferencePostProcessor(RpcClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    //保存发布的引用bean信息
    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new ConcurrentHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionname:beanFactory.getBeanDefinitionNames()){
            //遍历bean,然后获取到加载的bean，然后查看这些bean中有哪些携带了TinyRemoteReference注解
            //如果有，则构建一个动态代理实现
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionname);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null){
                //和forName方法相同，内部就是直接调用的forName
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName,this.classLoader);
                //针对当前类的指定字段，动态创建一个Bean
                ReflectionUtils.doWithFields(clazz,this::parseRpcReference);
            }
        }
        BeanDefinitionRegistry registry =   (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName,beanDefinition)->{
            if (context.containsBean(beanName)){
                log.warn("SpringContext already register bean {}",beanName);
                return;
            }
            //动态创建的bean注册容器内
            registry.registerBeanDefinition(beanName,beanDefinition);
            log.info("registered RpcReferenceBean {} success",beanName);
        });
    }

    private void parseRpcReference(Field field){
        TinyRemoteReference tinyRemoteReference = AnnotationUtils.getAnnotation(field, TinyRemoteReference.class);
        if (tinyRemoteReference != null){
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringRpcReferenceBean.class);
            builder.setInitMethodName(RpcContant.INIT_METHOD_NAME);
            builder.addPropertyValue("interfaceClass",field.getType());
            builder.addPropertyValue("serviceAddress",clientProperties.getServiceAddress());
            builder.addPropertyValue("servicePort",clientProperties.getServicePort());
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(),beanDefinition);
        }
    }
}

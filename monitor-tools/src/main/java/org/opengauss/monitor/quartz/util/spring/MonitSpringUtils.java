/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.quartz.util.spring;

import org.opengauss.monitor.exception.ParamsException;
import org.opengauss.monitor.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
@Component
public final class MonitSpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory configurableListableBeanFactory;

    private static ApplicationContext monitorContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        MonitSpringUtils.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        MonitSpringUtils.monitorContext = context;
    }

    /**
     * 获取对象
     *
     * @param str str
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getStr(String str) {
        try {
            return (T) configurableListableBeanFactory.getBean(str);
        } catch (BeansException exception) {
            throw new ParamsException(exception.getMessage());
        }
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param monitorClass monitorClass
     * @return t
     * @throws BeansException BeansException
     */
    public static <T> T getClass(Class<T> monitorClass) {
        try {
            return (T) configurableListableBeanFactory.getBean(monitorClass);
        } catch (BeansException exception) {
            throw new ParamsException(exception.getMessage());
        }
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param str str
     * @return boolean
     */
    public static boolean monitorBean(String str) {
        return configurableListableBeanFactory.containsBean(str);
    }

    /**
     * isSingleton
     *
     * @param str str
     * @return boolean
     */
    public static boolean isMonitorSingleton(String str) {
        return configurableListableBeanFactory.isSingleton(str);
    }

    /**
     * getType
     *
     * @param name name
     * @return Class注册对象的类型
     */
    public static Class<?> getMonitorType(String name) {
        return configurableListableBeanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name name
     * @return String[]
     */
    public static String[] getMonitorAliases(String name) {
        return configurableListableBeanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker invoker
     * @return t
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getMonitorProfiles() {
        return monitorContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getMonitorProfile() {
        final String[] activeProfiles = getMonitorProfiles();
        return StringUtils.isHaveSomething(activeProfiles) ? activeProfiles[0] : "";
    }

    /**
     * 获取配置文件中的值
     *
     * @param str 配置文件的key
     * @return 当前的配置文件的值
     */
    public static String getMonitorProperty(String str) {
        return monitorContext.getEnvironment().getRequiredProperty(str);
    }
}

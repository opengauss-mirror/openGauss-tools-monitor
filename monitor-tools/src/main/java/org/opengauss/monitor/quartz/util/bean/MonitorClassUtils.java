/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.quartz.util.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public class MonitorClassUtils extends org.springframework.beans.BeanUtils {
    /**
     * Bean属性复制工具方法。
     *
     * @param tar 目标对象
     * @param source  源对象
     */
    public static void attributeCopy(Object tar, Object source) {
        try {
            copyProperties(source, tar);
        } catch (BeansException exception) {
            log.error("attributeCopy-->{}", exception.getMessage());
        }
    }
}

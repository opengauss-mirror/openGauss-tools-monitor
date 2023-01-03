/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.service;

import org.opengauss.monitor.entity.SysConfig;
import org.opengauss.monitor.quartz.domain.SysJob;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public interface MeterService {
    /**
     * publish
     *
     * @param list list
     * @param sysConfig sysConfig
     * @param task task
     * @param sysJob sysJob
     */
    void publish(List<Map<String, Object>> list, SysConfig sysConfig, String task, SysJob sysJob);
}


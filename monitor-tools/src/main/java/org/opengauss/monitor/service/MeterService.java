/*
 * Copyright (c) 2026 Huawei Technologies Co., Ltd.
 * openGauss-tools-monitor is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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


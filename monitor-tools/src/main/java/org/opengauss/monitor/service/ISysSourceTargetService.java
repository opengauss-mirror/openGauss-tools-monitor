/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.service;

import org.opengauss.monitor.entity.MonitorResult;
import org.opengauss.monitor.entity.SysSourceTarget;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public interface ISysSourceTargetService {
    /**
     * selectAll
     *
     * @return MonitorResult
     */
    MonitorResult selectAll();

    /**
     * save
     *
     * @param sysSourceTarget sysSourceTarget
     * @return MonitorResult
     */
    MonitorResult save(SysSourceTarget sysSourceTarget);
}

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.quartz.util;

import org.quartz.JobExecutionContext;
import com.tools.monitor.quartz.domain.SysJob;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class MonitorJobExecution extends SummaryMonitorJob {
    @Override
    public void doExecute(JobExecutionContext context, SysJob sysJob) {
        MonitorInvokeUtil.invokeMethod(sysJob);
    }
}

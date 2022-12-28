/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.quartz.util;

import com.tools.monitor.common.contant.ScheduleCommon;
import com.tools.monitor.quartz.domain.SysJob;
import com.tools.monitor.quartz.util.bean.MonitorClassUtils;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public abstract class SummaryMonitorJob implements Job {
    /**
     * monitorDate
     */
    public static ThreadLocal<Date> monitorDate = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        SysJob monitorJob = new SysJob();
        MonitorClassUtils.attributeCopy(monitorJob, context.getMergedJobDataMap().get(ScheduleCommon.MONITOR_PROPERTIES));
        beginStart();
        if (jdege(monitorJob)) {
            doExecute(context, monitorJob);
        }
    }

    private Boolean jdege(SysJob sysJob){
        if(sysJob != null){
            return true;
        }
        return false;
    }

    /**
     * beginStart
     */
    public void beginStart() {
        monitorDate.set(new Date());
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    public abstract void doExecute(JobExecutionContext context, SysJob sysJob);
}

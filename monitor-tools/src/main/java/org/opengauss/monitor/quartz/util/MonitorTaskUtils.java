/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.quartz.util;

import org.opengauss.monitor.common.contant.ConmmonShare;
import org.opengauss.monitor.common.contant.ScheduleCommon;
import org.opengauss.monitor.exception.job.TaskException;
import org.opengauss.monitor.quartz.domain.SysJob;
import org.opengauss.monitor.quartz.util.spring.MonitSpringUtils;
import org.opengauss.monitor.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public class MonitorTaskUtils {
    /**
     * getMonitorClass
     *
     * @param job 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getMonitorClass(SysJob job) {
        boolean isSameTime = "0".equals(job.getConcurrent());
        if (isSameTime) {
            return MonitorJobExecution.class;
        } else {
            return QuartzMonitorExecution.class;
        }
    }

    /**
     * getTriggerKey
     *
     * @param num num
     * @param str str
     * @return TriggerKey
     */
    public static TriggerKey getMonitorKey(Long num, String str) {
        return TriggerKey.triggerKey(ScheduleCommon.MONITOR_CLASS_NAME + num, str);
    }

    /**
     * getMonitorWorkKey
     *
     * @param jobId    jobId
     * @param jobGroup jobGroup
     * @return JobKey
     */
    public static JobKey getMonitorWorkKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleCommon.MONITOR_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * createMonitorJob
     *
     * @param sch sch
     * @param task task
     * @throws SchedulerException SchedulerException
     * @throws TaskException      TaskException
     */
    public static void createMonitorJob(Scheduler sch, SysJob task) {
        try {
            Class<? extends Job> jobClass = getMonitorClass(task);
            // 构建job信息
            Long num = task.getJobId();
            String str = task.getJobGroup();
            JobDetail monitorDetail = JobBuilder.newJob(jobClass).withIdentity(getMonitorWorkKey(num, str)).build();
            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
            cronScheduleBuilder = handleMonitorMisfirePolicy(task, cronScheduleBuilder);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger cron = TriggerBuilder.newTrigger().withIdentity(getMonitorKey(num, str))
                    .withSchedule(cronScheduleBuilder).build();
            // 放入参数，运行时的方法可以获取
            monitorDetail.getJobDataMap().put(ScheduleCommon.MONITOR_PROPERTIES, task);
            // 判断是否存在
            if (sch.checkExists(getMonitorWorkKey(num, str))) {
                // 防止创建时存在数据问题 先移除，然后在执行创建操作
                sch.deleteJob(getMonitorWorkKey(num, str));
            }
            sch.scheduleJob(monitorDetail, cron);
            // 暂停任务
            if (task.getStatus().equals(ScheduleCommon.Status.PAUSE.getValue())) {
                sch.pauseJob(MonitorTaskUtils.getMonitorWorkKey(num, str));
            }
        } catch (TaskException | SchedulerException exception) {
            log.error("createMonitorJob fail-->{}", exception.getMessage());
        }
    }

    /**
     * handleMonitorMisfirePolicy
     *
     * @param task task
     * @param builder  builder
     * @return CronScheduleBuilder
     * @throws TaskException TaskException
     */
    public static CronScheduleBuilder handleMonitorMisfirePolicy(SysJob task, CronScheduleBuilder builder)
            throws TaskException {
        switch (task.getMisfirePolicy()) {
            case ScheduleCommon.MONITOR_DEFAULT:
                return builder;
            case ScheduleCommon.MONITOR_IGNORE_MISFIRES:
                return builder.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleCommon.MONITOR_FIRE_AND_PROCEED:
                return builder.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleCommon.MONITOR_DO_NOTHING:
                return builder.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + task.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", TaskException.Code.CONFIG_ERROR);
        }
    }

    /**
     * legalList
     *
     * @param str 目标字符串
     * @return 结果
     */
    public static boolean legalList(String str) {
        String name = StringUtils.substringBefore(str, "(");
        int num = StringUtils.countMatches(name, ".");
        if (num > 1) {
            return StringUtils.containsAnyIgnoreCase(str, ConmmonShare.JOB_WHITELIST_STR);
        }
        Object obj = MonitSpringUtils.getStr(StringUtils.split(str, ".")[0]);
        return StringUtils.containsAnyIgnoreCase(obj.getClass().getPackage().getName(), ConmmonShare.JOB_WHITELIST_STR);
    }
}

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.service;

import com.tools.monitor.entity.MonitorResult;
import com.tools.monitor.entity.ResponseVO;
import com.tools.monitor.entity.SysSourceTarget;
import com.tools.monitor.entity.TargetSource;
import com.tools.monitor.exception.job.TaskException;
import com.tools.monitor.quartz.domain.SysJob;
import org.quartz.SchedulerException;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public interface ISysJobService {
    /**
     * selectAllJob
     *
     * @param page page
     * @param size size
     * @param job  job
     * @return ResponseVO
     */
    ResponseVO selectAllJob(Integer page, Integer size, SysJob job);

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     * @throws SchedulerException SchedulerException
     */
    int pauseJob(SysJob job) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     * @throws SchedulerException SchedulerException
     */
    int resumeJob(SysJob job) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param task 调度信息
     * @return 结果
     */
    Boolean deleteTask(SysJob task);

    /**
     * 批量删除调度信息
     *
     * @param ids 需要删除的任务ID
     * @return 结果
     */
    void deleteTaskByIds(Long[] ids);

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     * @return 结果
     */
    void start(SysJob job);

    /**
     * 新增任务
     *
     * @param task 调度信息
     * @return 结果
     */
    MonitorResult insertTask(SysJob task);

    /**
     * 更新任务
     *
     * @param task 调度信息
     * @return 结果
     * @throws TaskException TaskException
     * @throws SchedulerException SchedulerException
     */
    MonitorResult updateTask(SysJob task);

    /**
     * batchPublish
     *
     * @param targetSource targetSource
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    MonitorResult batchPublish(TargetSource targetSource) throws SchedulerException;

    /**
     * 单个主机停止发布
     *
     * @param sourceTarget sourceTarget
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    MonitorResult singlePublishPause(SysSourceTarget sourceTarget);

    /**
     * 批量停止
     *
     * @param targetSource targetSource
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    MonitorResult batchPublishPause(TargetSource targetSource) throws SchedulerException;

    /**
     * 获取组别
     *
     * @return ResponseVO
     */
    ResponseVO selectGroup();

    /**
     * checkJobIds
     *
     * @param jobIds jobIds
     * @return ResponseVO
     */
    ResponseVO checkJobIds(Long[] jobIds);

    /**
     * getDefaultTarget
     *
     * @return MonitorResult
     */
    MonitorResult getDefaultTarget();
}

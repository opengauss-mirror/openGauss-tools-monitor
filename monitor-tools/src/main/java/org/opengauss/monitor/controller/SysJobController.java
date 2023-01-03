/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.controller;

import org.opengauss.monitor.common.core.controller.BasicController;
import org.opengauss.monitor.entity.MonitorResult;
import org.opengauss.monitor.entity.ResponseVO;
import org.opengauss.monitor.entity.SysSourceTarget;
import org.opengauss.monitor.entity.TargetSource;
import org.opengauss.monitor.exception.job.TaskException;
import org.opengauss.monitor.quartz.domain.SysJob;
import org.opengauss.monitor.service.ISysJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
@RestController
@RequestMapping("/monitor/job")
public class SysJobController extends BasicController {
    @Autowired
    private ISysJobService jobService;

    /**
     * list
     *
     * @param page page
     * @param size size
     * @param job job
     * @return ResponseVO
     */
    @PostMapping("/list/{page}/{size}")
    public ResponseVO list(@PathVariable Integer page, @PathVariable Integer size, @RequestBody SysJob job) {
        return jobService.selectAllJob(page, size, job);
    }

    /**
     * getDefaultTarget
     *
     * @return MonitorResult
     */
    @GetMapping("/list/default/target")
    public MonitorResult getDefaultTarget() {
        return jobService.getDefaultTarget();
    }

    /**
     * getGroup
     *
     * @return ResponseVO
     */
    @GetMapping(value = "/group")
    public ResponseVO getGroup() {
        return jobService.selectGroup();
    }

    /**
     * add
     *
     * @param job job
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     * @throws TaskException TaskException
     */
    @PostMapping(value = "/create")
    public MonitorResult add(@Validated @RequestBody SysJob job) throws SchedulerException, TaskException {
        job.setIsCreate(true);
        return jobService.insertTask(job);
    }

    /**
     * 修改指标
     *
     * @param job job
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     * @throws TaskException TaskException
     */
    @PostMapping(value = "/update")
    public MonitorResult edit(@Validated @RequestBody SysJob job) throws SchedulerException, TaskException {
        job.setIsCreate(false);
        return jobService.updateTask(job);
    }

    /**
     * 批量发布
     *
     * @param targetSource targetSource
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    @PostMapping("/batch/publish")
    public MonitorResult batchPublish(@RequestBody TargetSource targetSource) throws SchedulerException {
        return jobService.batchPublish(targetSource);
    }

    /**
     * 单主机停止发布
     *
     * @param sourceTarget sourceTarget
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    @PostMapping("/single/publish/pause")
    public MonitorResult singlePublishPause(@Validated @RequestBody SysSourceTarget sourceTarget) {
        return jobService.singlePublishPause(sourceTarget);
    }

    /**
     * 批量停止发布
     *
     * @param sourceTargets sourceTargets
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    @PostMapping("/batch/publish/pause")
    public MonitorResult batchPublishPause(@RequestBody TargetSource sourceTargets) throws SchedulerException {
        return jobService.batchPublishPause(sourceTargets);
    }

    /**
     * run
     *
     * @param job job
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     */
    @PutMapping("/run")
    public MonitorResult run(@Validated @RequestBody SysJob job) throws SchedulerException {
        jobService.start(job);
        return MonitorResult.success();
    }

    /**
     * 单指标删除
     *
     * @param jobIds jobIds
     * @return MonitorResult
     * @throws SchedulerException SchedulerException
     * @throws TaskException TaskException
     */
    @PostMapping("/delete")
   public MonitorResult remove(@RequestBody Long[] jobIds) throws SchedulerException, TaskException {
        jobService.deleteTaskByIds(jobIds);
        return MonitorResult.success("删除成功");
    }

    /**
     * check
     *
     * @param jobIds jobIds
     * @return ResponseVO
     * @throws SchedulerException SchedulerException
     * @throws TaskException TaskException
     */
    @PostMapping("/check")
    public ResponseVO check(@RequestBody Long[] jobIds) throws SchedulerException, TaskException {
        return jobService.checkJobIds(jobIds);
    }
}

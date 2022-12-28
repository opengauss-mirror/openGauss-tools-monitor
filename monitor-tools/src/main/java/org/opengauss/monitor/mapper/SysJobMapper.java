/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.opengauss.monitor.common.contant.ConmmonShare;
import org.opengauss.monitor.config.FileConfig;
import org.opengauss.monitor.entity.JsonTask;
import org.opengauss.monitor.quartz.domain.SysJob;
import org.opengauss.monitor.util.AssertUtil;
import org.opengauss.monitor.util.JsonUtilData;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Service
public class SysJobMapper {
    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    public List<SysJob> selectJobAll() {
        JsonTask jsonTask = JsonUtilData.jsonFileToObject(FileConfig.getTaskConfig(), JsonTask.class);
        if (jsonTask == null || CollectionUtil.isEmpty(jsonTask.getSysJobs())) {
            return new ArrayList<>();
        }
        return jsonTask.getSysJobs();
    }

    /**
     * 通过调度ID查询调度任务信息
     *
     * @param jobId 调度ID
     * @return 角色对象信息
     */
    public SysJob selectJobById(Long jobId) {
        List<SysJob> sysJobList = selectJobAll();
        SysJob sysJob = null;
        if (CollectionUtil.isNotEmpty(sysJobList)) {
            sysJob = sysJobList.stream().filter(s -> Objects.equals(s.getJobId(), jobId)).findFirst().orElse(null);
        }
        return sysJob;
    }

    /**
     * 通过调度IDS批量查询调度任务信息
     *
     * @param ids 调度ID
     * @return 角色对象信息
     */
    public List<SysJob> selectBatchJobByIds(List<Long> ids) {
        List<SysJob> sysJobs = selectJobAll();
        List<SysJob> result = new ArrayList<>();
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        for (Long id : ids) {
            for (SysJob sysJob : sysJobs) {
                if (id.equals(sysJob.getJobId())) {
                    result.add(sysJob);
                }
            }
        }
        return result;
    }


    /**
     * 批量删除调度任务信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public Boolean deleteJobByIds(List<Long> ids) {
        List<SysJob> sysJobs = selectJobAll();
        List<SysJob> result = new ArrayList<>();
        for (SysJob sysJob : sysJobs) {
            for (Long id : ids) {
                if (id.equals(sysJob.getJobId())) {
                    result.add(sysJob);
                }
            }
        }
        Boolean isDelete = sysJobs.removeAll(result);
        JsonTask jsonTask = new JsonTask();
        jsonTask.setSysJobs(sysJobs);
        JsonUtilData.objectToJsonFile(FileConfig.getTaskConfig(), jsonTask);
        return isDelete;
    }

    /**
     * 修改调度任务状态Status
     *
     * @param job 调度任务信息
     * @return 结果
     */
    public int updateJob(SysJob job) {
        List<SysJob> sysJobs = selectJobAll();
        AssertUtil.isTrue(CollectionUtil.isEmpty(sysJobs), "没有定时任务");
        SysJob sysJob = sysJobs.stream()
                .filter(item -> ObjectUtil.isNotEmpty(item) && item.getJobId().equals(job.getJobId()))
                .findFirst().orElse(null);
        if (sysJob == null) {
            return 0;
        }
        for (SysJob item : sysJobs) {
            if (item.getJobId().equals(job.getJobId())) {
                item.setStatus(job.getStatus());
            }
        }
        JsonTask jsonTask = new JsonTask();
        jsonTask.setSysJobs(sysJobs);
        JsonUtilData.objectToJsonFile(FileConfig.getTaskConfig(), jsonTask);
        return 1;
    }

    /**
     * 新增调度任务信息
     *
     * @param job 调度任务信息
     * @return 结果
     */
    public int insertJob(SysJob job) {
        List<SysJob> sysJobs = selectJobAll();
        sysJobs.add(job);
        JsonTask jsonTask = new JsonTask();
        jsonTask.setSysJobs(sysJobs);
        JsonUtilData.objectToJsonFile(FileConfig.getTaskConfig(), jsonTask);
        return 1;
    }

    /**
     * getGroup
     *
     * @return list
     */
    public List<String> getGroup() {
        List<SysJob> sysJobs = selectJobAll();
        if (CollectionUtil.isEmpty(sysJobs)) {
            return new ArrayList<>();
        }
        List<String> list = sysJobs.stream().filter(item -> !item.getTargetGroup().equals(ConmmonShare.SYSTEMTARGET))
                .map(SysJob::getTargetGroup).distinct().collect(Collectors.toList());
        return list;
    }
}

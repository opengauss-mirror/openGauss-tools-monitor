/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.quartz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.opengauss.monitor.common.contant.ScheduleCommon;
import org.opengauss.monitor.entity.BasicEntity;
import java.util.List;
import lombok.Data;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class SysJob extends BasicEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long jobId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dataSourceId;

    private Boolean isCreate;

    private Boolean isFalse;

    private String target;

    private Integer num;

    private String timeType;

    private Long time;

    private String startTime;

    /**
     * 任务名称
     */
    private String jobName = "sql";

    /**
     * 任务组名
     */
    private String jobGroup = "DEFAULT";

    /**
     * 指标组名
     */
    private String targetGroup;

    /**
     * 监控平台
     */
    private String platform;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * cron计划策略
     */
    private String misfirePolicy = ScheduleCommon.MONITOR_IGNORE_MISFIRES;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    private String concurrent = "0";

    private String status;

    private Boolean isPbulish = false;

    private Boolean isCanUpdate;

    private List<String> column;

    private Boolean isManagement;

    private List<String> timeInterval;
}

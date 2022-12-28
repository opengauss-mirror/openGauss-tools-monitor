/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tools.monitor.quartz.domain.SysJob;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class SysConfig {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dataSourceId;

    private String connectName;

    private String ip;

    private String containerIp;

    private String containerPort;

    private String serverIp;

    private String clientIp;

    private String port;

    private String driver;

    private String userName;

    private String serverName;

    private String clientName;

    private String password;

    private String serverPassword;

    private String clientPassword;

    private String clientPath;

    private String serverPath;

    private String url;

    private String createTime;

    private String dataBaseName;

    /**
     * 监控平台
     */
    private String platform;

    /**
     * 指标id集合
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> jobIds;

    /**
     * 指标id集合
     */
    private List<SysJob> targets;

    /**
     * 最后一次发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastReleaseTime;

    private Long time;

    private Boolean isCreate;
}

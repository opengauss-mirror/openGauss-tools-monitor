/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class SysSourceTarget {
    /**
     * 主机id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dataSourceId;

    /**
     * 指标id集合
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> jobIds;

    /**
     * 最后一次发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastReleaseTime;
}

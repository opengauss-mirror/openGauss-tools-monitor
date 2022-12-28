/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.entity;

import org.opengauss.monitor.quartz.domain.SysJob;
import lombok.Data;

import java.util.List;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class JsonTask {
    private List<SysJob> sysJobs;
}

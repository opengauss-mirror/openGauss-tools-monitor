/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class TargetSource {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> dataSourceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> jobIds;
}

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.entity.zabbix;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class SourceName {
    private String connectName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dataSourceId;
}

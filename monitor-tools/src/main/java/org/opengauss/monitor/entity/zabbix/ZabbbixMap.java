/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.entity.zabbix;

import lombok.Data;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class ZabbbixMap {
    private String zabbixTargetName;

    private String zabbixKey;

    private String zabbixTargetProcess;

    /**
     * ZabbbixMap
     */
    public ZabbbixMap() {
    }

    public ZabbbixMap(String zabbixTargetName, String zabbixKey, String zabbixTargetProcess) {
        this.zabbixTargetName = zabbixTargetName;
        this.zabbixKey = zabbixKey;
        this.zabbixTargetProcess = zabbixTargetProcess;
    }
}

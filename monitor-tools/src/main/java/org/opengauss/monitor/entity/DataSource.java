/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.entity;

import lombok.Data;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Data
public class DataSource {
    String id;
    String name;
    String url;
    String username;
    String password;
    String driver;
}

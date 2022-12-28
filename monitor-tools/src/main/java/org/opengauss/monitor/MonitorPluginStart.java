/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 */
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MonitorPluginStart {
	public static void main(String[] args) {
		SpringApplication.run(MonitorPluginStart.class, args);
	}
}

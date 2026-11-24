/*
 * Copyright (c) 2026 Huawei Technologies Co., Ltd.
 * openGauss-tools-monitor is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.opengauss.monitor.manager;

import org.opengauss.monitor.quartz.util.spring.MonitSpringUtils;
import org.opengauss.monitor.util.MonitorThreads;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class MonitorManager {
    private static MonitorManager manager = new MonitorManager();

    /**
     * 操作延迟10毫秒
     */
    private static final int OPERATION_DELAY = 10;

    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService monitorExecutor = MonitSpringUtils.getStr("monitorExecutorService");

    /**
     * 单例模式
     */
    private MonitorManager() {
    }

    /**
     * me
     *
     * @return AsyncManager
     */
    public static MonitorManager mine() {
        return manager;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void work(TimerTask task) {
        monitorExecutor.schedule(task, OPERATION_DELAY, TimeUnit.MILLISECONDS);
    }

    /**
     * stopTask
     */
    public void stopTask() {
        MonitorThreads.monitorShutdown(monitorExecutor);
    }
}

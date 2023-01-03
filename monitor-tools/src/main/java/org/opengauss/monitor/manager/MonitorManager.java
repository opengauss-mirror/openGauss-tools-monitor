/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
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

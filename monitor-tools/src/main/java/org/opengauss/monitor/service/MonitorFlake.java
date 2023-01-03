/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.service;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public class MonitorFlake {
    /**
     * 起始的时间戳（可设置当前时间之前的邻近时间）
     */
    private static final long START_STAMP = 1480166465631L;

    private static final long MONITOR_BIT = 12L;

    private static final long MECHANICAL_BIT = 5L;

    private static final long SNOW_FLAKE_CENTER_BIT = 5L;

    private static final long MAX_SNOW_FLAKE_NUM = ~(-1L << SNOW_FLAKE_CENTER_BIT);

    private static final long MAX_SNOW_FLAKE_MACHINE_NUM = ~(-1L << MECHANICAL_BIT);

    private static final long MAX_ESPECIALLY  = ~(-1L << MONITOR_BIT);

    private static final long MONITOR_LEFT = MONITOR_BIT;

    private static final long FLAKE_MONITOR_LEFT = MONITOR_BIT + MECHANICAL_BIT;

    private static final long SNOW_LEFT = FLAKE_MONITOR_LEFT + SNOW_FLAKE_CENTER_BIT;

    /**
     * 数据中心ID(0~31)
     */
    private final long monitorCenterId;

    /**
     * 工作机器ID(0~31)
     */
    private final long monitorId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long queue = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastGeneratorTime = -1L;

    /**
     * MonitorFlake
     *
     * @param monitorCenterId monitorCenterId
     * @param monitorMachineId    monitorMachineId
     */
    public MonitorFlake(long monitorCenterId, long monitorMachineId) {
        if (monitorCenterId > MAX_SNOW_FLAKE_NUM || monitorCenterId < 0) {
            throw new IllegalArgumentException("monitorCenterId can't be greater than MAX_SNOW_FLAKE_MACHINE_NUM or less than 0");
        }
        if (monitorMachineId > MAX_SNOW_FLAKE_MACHINE_NUM || monitorMachineId < 0) {
            throw new IllegalArgumentException("monitorMachineId can't be greater than MAX_SNOW_FLAKE_MACHINE_NUM or less than 0");
        }
        this.monitorCenterId = monitorCenterId;
        this.monitorId = monitorMachineId;
    }

    /**
     * nextId
     *
     * @return long
     */
    public synchronized long nextId() {
        long time = getMonitorewStamp();
        if (time < lastGeneratorTime) {
            log.error("The clock is moved backward. refuse to generate id");
        }
        if (time == lastGeneratorTime) {
            // 相同毫秒内，序列号自增
            queue = (queue + 1) & MAX_ESPECIALLY;
            // 同一毫秒的序列数已经达到最大
            if (queue == 0L) {
                // 阻塞到下一个毫秒,获得新的时间戳
                time = getNextFactory();
            }
        } else {
            // 不同毫秒内，序列号置为0
            queue = 0L;
        }
        lastGeneratorTime = time;
        // 移位并通过或运算拼到一起组成64位的ID
        return (time - START_STAMP) << SNOW_LEFT
                | monitorCenterId << FLAKE_MONITOR_LEFT
                | monitorId << MONITOR_LEFT
                | queue;
    }

    /**
     * getNextFactory
     *
     * @return long
     */
    private long getNextFactory() {
        long factory = getMonitorewStamp();
        while (factory <= lastGeneratorTime) {
            factory = getMonitorewStamp();
        }
        return factory;
    }

    /**
     * getNewStamp
     *
     * @return long
     */
    private long getMonitorewStamp() {
        return System.currentTimeMillis();
    }
}


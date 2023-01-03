/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.manager.factory;

import org.opengauss.monitor.entity.SysConfig;
import org.opengauss.monitor.mapper.SysSourceTargetMapper;
import org.opengauss.monitor.quartz.domain.SysJob;
import org.opengauss.monitor.quartz.task.MonitorTask;
import org.opengauss.monitor.quartz.util.spring.MonitSpringUtils;
import org.opengauss.monitor.service.impl.MeterServiceImpl;
import org.opengauss.monitor.service.impl.SysJobServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class AsyncFactory {
    /**
     * recordZabbix
     *
     * @param zabbix       zabbix
     * @param sysConfig    sysConfig
     * @param zabbixConfig zabbixConfig
     * @return TimerTask
     */
    public static TimerTask recordZabbix(final List<SysJob> zabbix,
        final SysConfig sysConfig,
        final SysConfig zabbixConfig) {
        return new TimerTask() {
            @Override
            public void run() {
                MonitSpringUtils.getClass(SysJobServiceImpl.class).executeZabbix(zabbix, sysConfig, zabbixConfig);
            }
        };
    }

    /**
     * removeRegistry
     *
     * @param gaugeName gaugeName
     * @return TimerTask
     */
    public static TimerTask removeRegistry(final List<String> gaugeName) {
        return new TimerTask() {
            /**
             * run
             */
            @Override
            public void run() {
                MonitSpringUtils.getClass(MeterServiceImpl.class).removeRegister(gaugeName);
            }
        };
    }

    /**
     * removeJobId
     *
     * @param jobid jobid
     * @return TimerTask
     */
    public static TimerTask removeJobId(final Long jobid) {
        return new TimerTask() {
            @Override
            public void run() {
                MonitSpringUtils.getClass(SysSourceTargetMapper.class).removeJobids(jobid);
            }
        };
    }

    /**
     * executeOne
     *
     * @param params params
     * @param name   name
     * @param jobId  jobId
     * @return TimerTask
     */
    public static TimerTask executeOne(final String params, final String name, final Long jobId) {
        return new TimerTask() {
            /**
             * run
             */
            @Override
            public void run() {
                MonitSpringUtils.getClass(MonitorTask.class).targetParams(params, name, jobId);
            }
        };
    }

    /**
     * reportNagios
     *
     * @param nagiosMap nagiosMap
     * @return TimerTask
     */
    public static TimerTask reportNagios(final Map<String, Object> nagiosMap) {
        return new TimerTask() {
            /**
             * run
             */
            @Override
            public void run() {
                MonitSpringUtils.getClass(MeterServiceImpl.class).reportNagios(nagiosMap);
            }
        };
    }

    /**
     * recordNagios
     *
     * @param nagios       nagios
     * @param sysConfig    sysConfig
     * @param nagiosConfig nagiosConfig
     * @return TimerTask
     */
    public static TimerTask recordNagios(final List<SysJob> nagios,
        final SysConfig sysConfig,
        final SysConfig nagiosConfig) {
        return new TimerTask() {
            /**
             * run
             */
            @Override
            public void run() {
                MonitSpringUtils.getClass(SysJobServiceImpl.class).publishNagios(nagios, sysConfig, nagiosConfig);
            }
        };
    }

    /**
     * executeNagios
     *
     * @param nagios nagios
     * @return TimerTask
     */
    public static TimerTask executeNagios(final List<SysJob> nagios) {
        return new TimerTask() {
            /**
             * run
             */
            @Override
            public void run() {
                MonitSpringUtils.getClass(SysJobServiceImpl.class).startNagios(nagios);
            }
        };
    }
}

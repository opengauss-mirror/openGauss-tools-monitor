/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.mapper;

import ch.ethz.ssh2.Connection;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.opengauss.monitor.common.contant.ConmmonShare;
import org.opengauss.monitor.config.FileConfig;
import org.opengauss.monitor.config.PostgresqlConfig;
import org.opengauss.monitor.config.ZabbixConfig;
import org.opengauss.monitor.entity.JsonConfig;
import org.opengauss.monitor.entity.SysConfig;
import org.opengauss.monitor.entity.SysSourceTarget;
import org.opengauss.monitor.service.MonitorFlake;
import org.opengauss.monitor.service.impl.NagiosServiceImpl;
import org.opengauss.monitor.util.Base64;
import org.opengauss.monitor.util.ConnectionUtil;
import org.opengauss.monitor.util.JsonUtilData;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Service
@Slf4j
public class SysConfigMapper {
    private static Connection conn;

    private static final Connection EMPY = null;

    private static final String LINE_FEED = String.valueOf(StrUtil.C_SLASH);

    private org.opengauss.monitor.service.MonitorFlake MonitorFlake = new MonitorFlake(11, 11);

    @Value("${date.pattern}")
    private String dataPattern;

    @Autowired
    private NagiosServiceImpl nagiosService;

    @Autowired
    private SysSourceTargetMapper sourceTargetMapper;

    /**
     * getAllConfig
     *
     * @return list
     */
    public List<SysConfig> getAllConfig() {
        JsonConfig jsonConfig = JsonUtilData.jsonFileToObject(FileConfig.getDataSourceConfig(), JsonConfig.class);
        if (jsonConfig == null || CollectionUtil.isEmpty(jsonConfig.getSysConfigs())) {
            return new ArrayList<>();
        }
        return jsonConfig.getSysConfigs();
    }

    /**
     * getConfigByid
     *
     * @param id id
     * @return SysConfig
     */
    public SysConfig getConfigByid(Long id) {
        List<SysConfig> sysConfigList = getAllConfig();
        SysConfig config = null;
        if (CollectionUtil.isNotEmpty(sysConfigList)) {
            config = sysConfigList.stream()
                    .filter(s -> Objects.equals(s.getDataSourceId(), id))
                    .findFirst().orElse(null);
        }
        return config;
    }

    /**
     * getZabbixConfig
     *
     * @return SysConfig
     */
    public SysConfig getZabbixConfig() {
        List<SysConfig> sysConfigList = getAllConfig();
        List<SysConfig> zabbix = new ArrayList<>();
        SysConfig config = null;
        if (CollectionUtil.isNotEmpty(sysConfigList)) {
            zabbix = sysConfigList.stream()
                    .filter(item -> item.getPlatform().equals(ConmmonShare.ZABBIX))
                    .collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(zabbix)) {
            config = zabbix.get(0);
            config.setPassword(Base64.decode(config.getPassword()));
        }
        return config;
    }

    /**
     * getNagiosConfig
     *
     * @return SysConfig
     */
    public SysConfig getNagiosConfig() {
        List<SysConfig> sysConfigList = getAllConfig();
        List<SysConfig> nagios = new ArrayList<>();
        SysConfig config = null;
        if (CollectionUtil.isNotEmpty(sysConfigList)) {
            nagios = sysConfigList.stream()
                    .filter(item -> item.getPlatform().equals(ConmmonShare.NAGIOS))
                    .collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(nagios)) {
            config = nagios.get(0);
            config.setClientPassword(Base64.decode(config.getClientPassword()));
            config.setServerPassword(Base64.decode(config.getServerPassword()));
        }
        return config;
    }

    /**
     * getBatchById
     *
     * @param ids ids
     * @return list
     */
    public List<SysConfig> getBatchById(List<Long> ids) {
        List<SysConfig> sysConfigList = getAllConfig();
        List<SysConfig> result = new ArrayList<>();
        if (CollectionUtil.isEmpty(ids)) {
            return result;
        }
        for (Long id : ids) {
            for (SysConfig sysConfig : sysConfigList) {
                if (id.equals(sysConfig.getDataSourceId())) {
                    result.add(sysConfig);
                }
            }
        }
        return result;
    }

    /**
     * saveConfig
     *
     * @param sysConfig sysConfig
     * @return String
     */
    public String saveConfig(SysConfig sysConfig) {
        List<SysConfig> sysConfigList = getAllConfig();
        if (ConmmonShare.NAGIOS.equals(sysConfig.getPlatform())) {
            Boolean isServer =
                    getConnection(sysConfig.getServerIp(), sysConfig.getServerName(), sysConfig.getServerPassword());
            if (!isServer) {
                return "服务端连接失败!";
            } else if (StrUtil.isNotBlank(checkServerPath(sysConfig))) {
                return "服务端路径不正确";
            } else {
                log.info("saveConfig");
            }
            Boolean isClient =
                    getConnection(sysConfig.getClientIp(), sysConfig.getClientName(), sysConfig.getClientPassword());
            if (!isClient) {
                return "客户端连接失败!";
            } else if (StrUtil.isNotBlank(checkClientPath(sysConfig))) {
                return "客户端路径不正确";
            } else {
                log.info("saveConfig");
            }
            // 校验路径
            sysConfig.setClientPassword(Base64.encode(sysConfig.getClientPassword()));
            sysConfig.setServerPassword(Base64.encode(sysConfig.getServerPassword()));
            sysConfig.setDataSourceId(MonitorFlake.nextId());
        } else {
            dealSysConfig(sysConfig);
            // 重复性校验
            String message = checkConfig(sysConfig, sysConfigList);
            if (StrUtil.isNotBlank(message)) {
                return message;
            }
            String msg = ConnectionUtil.getConnection(sysConfig);
            if (StrUtil.isNotBlank(msg)) {
                return msg;
            }
            sysConfig.setPassword(Base64.encode(sysConfig.getPassword()));
        }
        List<SysConfig> zabbix = sysConfigList.stream()
                .filter(item -> item.getPlatform().equals(ConmmonShare.ZABBIX)).collect(Collectors.toList());
        if (ConmmonShare.ZABBIX.equals(sysConfig.getPlatform()) && CollectionUtil.isNotEmpty(zabbix)) {
            sysConfigList = deleteBatchByIds(Arrays.asList(zabbix.get(0).getDataSourceId()));
        }
        List<SysConfig> nagios = sysConfigList.stream()
                .filter(item -> item.getPlatform().equals(ConmmonShare.NAGIOS)).collect(Collectors.toList());
        if (ConmmonShare.NAGIOS.equals(sysConfig.getPlatform()) && CollectionUtil.isNotEmpty(nagios)) {
            sysConfigList = deleteBatchByIds(Arrays.asList(nagios.get(0).getDataSourceId()));
        }
        sysConfigList.add(sysConfig);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setSysConfigs(sysConfigList);
        JsonUtilData.objectToJsonFile(FileConfig.getDataSourceConfig(), jsonConfig);
        return "";
    }

    private String checkClientPath(SysConfig sysConfig) {
        Connection connection =
                getConn(sysConfig.getClientIp(), sysConfig.getClientName(), sysConfig.getClientPassword());
        String comd = "cat " + sysConfig.getClientPath() + LINE_FEED + "etc" + LINE_FEED + "nrpe.cfg";
        if (StrUtil.isEmpty(nagiosService.executeCmdAndGetResult(comd, connection))) {
            return "客户端路径不正确";
        }
        return "";
    }

    private String checkServerPath(SysConfig sysConfig) {
        Connection connection =
                getConn(sysConfig.getServerIp(), sysConfig.getServerName(), sysConfig.getServerPassword());
        String comd = "cat " + sysConfig.getServerPath() + LINE_FEED + "etc" + LINE_FEED + "nagios.cfg";
        if (StrUtil.isEmpty(nagiosService.executeCmdAndGetResult(comd, connection))) {
            return "服务端路径不正确";
        }
        return "";
    }

    /**
     * getConnection
     *
     * @param hostIp   hostIp
     * @param userName userName
     * @param password password
     * @return Boolean
     */
    public Boolean getConnection(String hostIp, String userName, String password) {
        // 设置执行响应时间的方法体
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Boolean isFlag = false;
                try {
                    conn = new Connection(hostIp);
                    conn.connect();
                    isFlag = conn.authenticateWithPassword(userName, password);
                } catch (IOException e) {
                    log.error("ssh_getConnection_{}", e.getMessage());
                } finally {
                    conn.close();
                }
                return isFlag.toString();
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        try {
            Future<String> future = threadPoolExecutor.submit(task);
            String obj = future.get(1500, TimeUnit.MILLISECONDS);
            if (obj.equals(false)) {
                return false;
            }
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            return false;
        } finally {
            threadPoolExecutor.shutdown();
        }
        return true;
    }

    /**
     * getConn
     *
     * @param hostIp   hostIp
     * @param userName userName
     * @param password password
     * @return Connection
     */
    public Connection getConn(String hostIp, String userName, String password) {
        boolean isFlag = false;
        try {
            Connection connection = new Connection(hostIp);
            connection.connect();
            isFlag = connection.authenticateWithPassword(userName, password);
            if (isFlag) {
                log.info("auth success!");
                return connection;
            } else {
                log.error(hostIp + "auth fail!");
                connection.close();
                return EMPY;
            }
        } catch (IOException exception) {
            return EMPY;
        }
    }

    /**
     * updateProm
     *
     * @param sysConfig sysConfig
     * @return String
     */
    public String updateProm(SysConfig sysConfig) {
        List<SysConfig> sysConfigList = getAllConfig();
        List<String> nameList = sysConfigList.stream().map(SysConfig::getConnectName).collect(Collectors.toList());
        dealSysConfig(sysConfig);
        // 重复性校验
        SysConfig sysConfig1 = getConfigByid(sysConfig.getDataSourceId());
        SysSourceTarget sysSourceTarget = sourceTargetMapper.sysSourceTargetById(sysConfig.getDataSourceId());
        if (!sysConfig.getIp().equals(sysConfig1.getIp())) {
            return "不允许修改Ip";
        }
        if (!sysConfig1.getConnectName().equals(sysConfig.getConnectName())
                && nameList.contains(sysConfig.getConnectName())) {
            return "实例名称不能重复";
        }
        String msg = ConnectionUtil.getConnection(sysConfig);
        if (StrUtil.isNotBlank(msg)) {
            return msg;
        }
        sysConfig.setPassword(Base64.encode(sysConfig.getPassword()));
        List<SysConfig> newList = deleteBatchByIds(Arrays.asList(sysConfig.getDataSourceId()));
        newList.add(sysConfig);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setSysConfigs(newList);
        JsonUtilData.objectToJsonFile(FileConfig.getDataSourceConfig(), jsonConfig);
        return "";
    }

    private String checkConfig(SysConfig sysConfig, List<SysConfig> sysConfigList) {
        List<String> connectName = sysConfigList.stream()
                .filter(item -> item.getPlatform().equals(ConmmonShare.PROM)).map(SysConfig::getConnectName)
                .collect(Collectors.toList());
        List<String> ips = sysConfigList.stream()
                .filter(item -> item.getPlatform().equals(ConmmonShare.PROM)).map(SysConfig::getIp)
                .collect(Collectors.toList());
        if (sysConfig.getPlatform().equals(ConmmonShare.ZABBIX)) {
            return "";
        }
        String name = connectName.stream()
                .filter(itme -> itme.equals(sysConfig.getConnectName()))
                .findFirst().orElse(null);
        String ip = ips.stream().filter(itme -> itme.equals(sysConfig.getIp())).findFirst().orElse(null);
        if (ObjectUtil.isNotEmpty(name)) {
            return "实例名称不能重复";
        }
        if (ObjectUtil.isNotEmpty(ip)) {
            return "实例ip不能重复";
        }
        return "";
    }

    private void dealSysConfig(SysConfig sysConfig) {
        if (ConmmonShare.PROM.equals(sysConfig.getPlatform())) {
            String url = PostgresqlConfig.getPrefix() + sysConfig.getIp()
                    + StrUtil.C_COLON + sysConfig.getPort() + PostgresqlConfig.getSuffix();
            sysConfig.setUrl(url);
            sysConfig.setDriver(PostgresqlConfig.getDriver());
        }
        if (ConmmonShare.ZABBIX.equals(sysConfig.getPlatform())) {
            String url = ZabbixConfig.getPrefix() + sysConfig.getIp() + StrUtil.C_COLON
                    + sysConfig.getPort() + StrUtil.SLASH + sysConfig.getDataBaseName() + ZabbixConfig.getSuffix();
            sysConfig.setUrl(url);
            sysConfig.setDriver(ZabbixConfig.getDriver());
        }
        if (sysConfig.getIsCreate()) {
            sysConfig.setDataSourceId(MonitorFlake.nextId());
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dataPattern);
        LocalDateTime ldt = LocalDateTime.now();
        sysConfig.setCreateTime(ldt.format(dtf));
        sysConfig.setTime(System.currentTimeMillis());
    }

    /**
     * deleteBatchByIds
     *
     * @param ids ids
     * @return list
     */
    public List<SysConfig> deleteBatchByIds(List<Long> ids) {
        List<SysConfig> sysConfigs = getAllConfig();
        List<SysConfig> result = new ArrayList<>();
        for (SysConfig sysConfig : sysConfigs) {
            for (Long id : ids) {
                if (ObjectUtil.isNotEmpty(id) && id.equals(sysConfig.getDataSourceId())) {
                    result.add(sysConfig);
                }
            }
        }
        Boolean isFlag = sysConfigs.removeAll(result);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setSysConfigs(sysConfigs);
        JsonUtilData.objectToJsonFile(FileConfig.getDataSourceConfig(), jsonConfig);
        return sysConfigs;
    }
}

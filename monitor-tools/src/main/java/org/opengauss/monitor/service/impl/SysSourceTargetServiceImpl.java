/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.service.impl;

import org.opengauss.monitor.entity.MonitorResult;
import org.opengauss.monitor.entity.SysSourceTarget;
import org.opengauss.monitor.mapper.SysSourceTargetMapper;
import org.opengauss.monitor.service.ISysSourceTargetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
@Service
@DependsOn("generatorFile")
public class SysSourceTargetServiceImpl implements ISysSourceTargetService {
    @Autowired
    private SysSourceTargetMapper sysSourceTargetMapper;

    @Override
    public MonitorResult selectAll() {
        return MonitorResult.success("succ");
    }

    @Override
    public MonitorResult save(SysSourceTarget sysSourceTarget) {
        sysSourceTargetMapper.save(sysSourceTarget);
        return MonitorResult.success("保存成功");
    }
}

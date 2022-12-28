/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.entity;

import cn.hutool.http.HttpStatus;
import org.opengauss.monitor.util.StringUtils;

import java.util.HashMap;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class MonitorResult extends HashMap<String, Object> {
    /**
     * code
     */
    public static final String CODE = "code";

    /**
     * message
     */
    public static final String MSG = "message";

    /**
     * Data object
     */
    public static final String DATA = "data";

    /**
     * Initializes a newly created MonitorResult object to represent an empty message
     */
    public MonitorResult() {
    }

    /**
     * Initialize a newly created MonitorResult object
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public MonitorResult(int code, String msg) {
        super.put(CODE, code);
        super.put(MSG, msg);
    }

    /**
     * Initialize a newly created MonitorResult object
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public MonitorResult(int code, String msg, Object data) {
        super.put(CODE, code);
        super.put(MSG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA, data);
        }
    }

    /**
     * Return success message
     *
     * @return 成功消息
     */
    public static MonitorResult success() {
        return MonitorResult.success("操作成功");
    }

    /**
     * success
     *
     * @param data data
     * @return MonitorResult
     */
    public static MonitorResult success(Object data) {
        return MonitorResult.success("操作成功", data);
    }

    /**
     * Return success message
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static MonitorResult success(String msg) {
        return MonitorResult.success(msg, null);
    }

    /**
     * Return success message
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static MonitorResult success(String msg, Object data) {
        return new MonitorResult(HttpStatus.HTTP_OK, msg, data);
    }

    /**
     * Return error message
     *
     * @return MonitorResult
     */
    public static MonitorResult error() {
        return MonitorResult.error("操作失败");
    }

    /**
     * Return error message
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static MonitorResult error(String msg) {
        return MonitorResult.error(msg, null);
    }

    /**
     * Return error message
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static MonitorResult error(String msg, Object data) {
        return new MonitorResult(HttpStatus.HTTP_BAD_REQUEST, msg, data);
    }

    /**
     * target
     *
     * @param msg msg
     * @return MonitorResult
     */
    public static MonitorResult target(String msg) {
        return new MonitorResult(600, msg);
    }

    /**
     * Return error message
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static MonitorResult error(int code, String msg) {
        return new MonitorResult(code, msg, null);
    }

    /**
     * Convenient chained call
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public MonitorResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

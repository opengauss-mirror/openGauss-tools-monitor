/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2020. All rights reserved.
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
public class ResponseVO {
    private Integer code;                       // 系统响应编码

    private String message;                     // 系统响应信息

    private Integer total;                      // 总条数

    private Object data;                        // 系统响应数据

    /**
     * ResponseVO
     *
     * @param code    code
     * @param message message
     * @param total   total
     * @param data    data
     */
    public ResponseVO(Integer code, String message, Integer total, Object data) {
        this.code = code;
        this.message = message;
        this.total = total;
        this.data = data;
    }

    /**
     * successResponseVO
     *
     * @param message message
     * @return ResponseVO
     */
    public static ResponseVO successResponseVO(String message) {
        return new ResponseVO(ResponseCode.RESPONSE_SUCCESS.getCode(), message, null, null);
    }

    /**
     * successResponseVO
     *
     * @param data data
     * @return ResponseVO
     */
    public static ResponseVO successResponseVO(Object data) {
        return new ResponseVO(ResponseCode.RESPONSE_SUCCESS.getCode(), ResponseCode.RESPONSE_SUCCESS.getRemark(), null, data);
    }

    /**
     * successResponseVO
     *
     * @param message message
     * @param data    data
     * @return ResponseVO
     */
    public static ResponseVO successResponseVO(String message, Object data) {
        return new ResponseVO(ResponseCode.RESPONSE_SUCCESS.getCode(), message, null, data);
    }

    /**
     * pageResponseVO
     *
     * @param total total
     * @param data  data
     * @return ResponseVO
     */
    public static ResponseVO pageResponseVO(int total, Object data) {
        return new ResponseVO(ResponseCode.RESPONSE_SUCCESS.getCode(),
                ResponseCode.RESPONSE_SUCCESS.getRemark(), total, data);
    }

    /**
     * errorResponseVO
     *
     * @param errorMsg errorMsg
     * @return ResponseVO
     */
    public static ResponseVO errorResponseVO(String errorMsg) {
        return new ResponseVO(ResponseCode.RESPONSE_ERROR.getCode(), errorMsg, null, "");
    }

    /**
     * errorTarget
     *
     * @param errorMsg errorMsg
     * @return ResponseVO
     */
    public static ResponseVO errorTarget(String errorMsg) {
        return new ResponseVO(ResponseCode.RESPONSE_TARGET_ERR.getCode(), errorMsg, null, "");
    }

    /**
     * exceptionResponseVO
     *
     * @param exception exception
     * @return ResponseVO
     */
    public static ResponseVO exceptionResponseVO(Exception exception) {
        return new ResponseVO(ResponseCode.RESPONSE_EXCEPTION.getCode(), exception.getMessage(), null, "data");
    }
}

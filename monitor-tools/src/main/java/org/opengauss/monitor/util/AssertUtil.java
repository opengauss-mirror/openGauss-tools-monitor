/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.util;

import org.opengauss.monitor.exception.ParamsException;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class AssertUtil {
    /**
     * isTrue
     *
     * @param isFlag isFlag
     * @param msg msg
     */
    public static void isTrue(Boolean isFlag, String msg) {
        if (isFlag) {
            throw new ParamsException(msg);
        }
    }
}

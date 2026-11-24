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

package org.opengauss.monitor.util;

import java.nio.charset.StandardCharsets;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class Base64 {
    /**
     * base64编码
     *
     * @param message 需要编码的内容
     * @return 编码之后的内容
     */
    public static String encode(String message) {
        java.util.Base64.Encoder encoder = java.util.Base64.getMimeEncoder();
        return encoder.encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64解码
     *
     * @param encodeMessage encodeMessage
     * @return 解码之后的内容
     */
    public static String decode(String encodeMessage) {
        java.util.Base64.Decoder decoder = java.util.Base64.getMimeDecoder();
        byte[] decode = decoder.decode(encodeMessage);
        return new String(decode, StandardCharsets.UTF_8);
    }
}

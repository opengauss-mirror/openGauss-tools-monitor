/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.util;

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

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
public class ZabbixMap {
    /**
     * getMapToString
     *
     * @param map map
     * @return String
     */
    public static String getMapToString(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        // 将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        // 给数组排序(升序)
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < keyArray.length; i++) {
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                sb.append(keyArray[i]).append("=")
                        .append("\"")
                        .append(String.valueOf(map.get(keyArray[i])).trim()).append("\"")
                        .append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}

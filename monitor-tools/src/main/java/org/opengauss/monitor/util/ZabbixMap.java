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

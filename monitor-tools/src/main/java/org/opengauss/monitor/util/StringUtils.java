/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package org.opengauss.monitor.util;

import org.opengauss.monitor.common.contant.ConmmonShare;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * nvl
     *
     * @param value        value
     * @param defaultValue defaultValue
     * @param <T>          t
     * @return t
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * isEmpty
     *
     * @param coll coll
     * @return boolean
     */
    public static boolean isNothing(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * isNotEmpty
     *
     * @param collection collection
     * @return boolean
     */
    public static boolean isHaveSomething(Collection<?> collection) {
        return !isNothing(collection);
    }

    /**
     * isEmpty
     *
     * @param objects objects
     * @return boolean
     */
    public static boolean isNothing(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * isNotEmpty
     *
     * @param objects objects
     * @return boolean
     */
    public static boolean isHaveSomething(Object[] objects) {
        return !isNothing(objects);
    }

    /**
     * isEmpty
     *
     * @param monitorMap monitorMap
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> monitorMap) {
        return isNull(monitorMap) || monitorMap.isEmpty();
    }

    /**
     * isNotEmpty
     *
     * @param monitorMap map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> monitorMap) {
        return !isEmpty(monitorMap);
    }

    /**
     * isEmpty
     *
     * @param str str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * isNotEmpty
     *
     * @param str str
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * isNull
     *
     * @param object object
     * @return boolean
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * isNotNull
     *
     * @param object object
     * @return boolean
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * isArray
     *
     * @param object object
     * @return boolean
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * trim
     *
     * @param str str
     * @return String
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param st   字符串
     * @param star 开始
     * @return 结果
     */
    public static String substring(final String st, int star) {
        String str = st;
        int start = star;
        if (str == null) {
            return NULLSTR;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }
        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param st   字符串
     * @param star 开始
     * @param en  结束
     * @return 结果
     */
    public static String substring(final String st, int star, int en) {
        String str = st;
        int start = star;
        int end = en;
        if (str == null) {
            return NULLSTR;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return NULLSTR;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    /**
     * ishttp
     *
     * @param link link
     * @return boolean
     */
    public static boolean ishttp(String link) {
        return StringUtils.startsWithAny(link, ConmmonShare.HTTP, ConmmonShare.HTTPS);
    }

    /**
     * 字符串转set
     *
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    public static final Set<String> str2Set(String str, String sep) {
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param sep         分隔符
     * @param isFilterBlank 过滤纯空白
     * @param isTrim        去掉首尾空白
     * @return list集合
     */
    public static final List<String> str2List(String str, String sep, boolean isFilterBlank, boolean isTrim) {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(str)) {
            return list;
        }
        // 过滤空白字符串
        if (isFilterBlank && StringUtils.isBlank(str)) {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split) {
            if (isFilterBlank && StringUtils.isBlank(string)) {
                continue;
            }
            if (isTrim) {
                string = string.trim();
            }
            list.add(string);
        }
        return list;
    }

    /**
     * containsAnyIgnoreCase
     *
     * @param cs                  cs
     * @param searchCharSequences searchCharSequences
     * @return boolean
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
        if (isEmpty(cs) || isNothing(searchCharSequences)) {
            return false;
        }
        for (CharSequence testStr : searchCharSequences) {
            if (containsIgnoreCase(cs, testStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * toUnderScoreCase
     *
     * @param str str
     * @return String
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean isPreCharIsUpperCase = true;
        // 当前字符是否大写
        boolean isCurreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean isNexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (i > 0) {
                isPreCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                isPreCharIsUpperCase = false;
            }
            isCurreCharIsUpperCase = Character.isUpperCase(ch);
            if (i < (str.length() - 1)) {
                isNexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if (isPreCharIsUpperCase && isCurreCharIsUpperCase && !isNexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !isPreCharIsUpperCase) && isCurreCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else {
                log.error("string");
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String all : strs) {
                if (str.equalsIgnoreCase(trim(all))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * convertToCamelCase
     *
     * @param name name
     * @return String
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
        } else {
            log.error("convertToCamelCase");
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase(Locale.ROOT));
            result.append(camel.substring(1).toLowerCase(Locale.ROOT));
        }
        return result.toString();
    }

    /**
     * toCamelCase
     *
     * @param st st
     * @return String
     */
    public static String toCamelCase(String st) {
        String str = st;
        if (str == null) {
            return "";
        }
        str = str.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder(str.length());
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == SEPARATOR) {
                isUpperCase = true;
            } else if (isUpperCase) {
                sb.append(Character.toUpperCase(ch));
                isUpperCase = false;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public class JsonUtilData {
    /**
     * Object 转换为 json 文件
     *
     * @param finalPath finalPath 是绝对路径 + 文件名，请确保欲生成的文件所在目录已创建好
     * @param object    需要被转换的 Object
     */
    public static void objectToJsonFile(String finalPath, Object object) {
        String json = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
        try {
            FileOutputStream fos = new FileOutputStream(new File(finalPath));
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(json);
            // 注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException exception) {
            log.error("object--->jsonFile--->{}", "Entity class conversion json failed");
        }
    }

    /**
     * json 文件转换为 Object
     *
     * @param finalPath   finalPath 是绝对路径 + 文件名，请确保欲生成的文件所在目录已创建好
     * @param targetClass 需要被转换的 json 对应的目标类
     * @param <T>         需要被转换的 json 对应的目标类
     * @return 解析后的 Object
     */
    public static <T> T jsonFileToObject(String finalPath, Class<T> targetClass) {
        String jsonString;
        T t = null;
        File file = new File(".", finalPath);
        if (!FileUtil.isNotEmpty(file)) {
            return t;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
            return JSON.parseObject(jsonString, targetClass);
        } catch (IOException exception) {
            log.error("json-->object-->fail{}", "json conversion Entity class failed");
        }
        return t;
    }
}

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 */

package com.tools.monitor.util;

import com.tools.monitor.exception.ParamsException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * 功能描述
 *
 * @author liu
 * @since 2022-10-01
 */
@Slf4j
public class FileUtil {
    /**
     * createFolder
     *
     * @param folderPath folderPath
     * @return File
     */
    public static File createFolder(String folderPath) {
        File folder = null;
        // 根据路径创建 File 对象
        folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
     * createFile
     *
     * @param filePath filePath
     * @return File
     */
    public static File createFile(String filePath) {
        // 根据路径创建 File 对象
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException exception) {
            log.error("createFile-->{}", exception.getMessage());
            throw new ParamsException("createFile fail");
        }
        return file;
    }
}

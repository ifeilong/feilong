/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.io.filenameutil;

import com.feilong.io.FilenameUtil;
import com.feilong.lib.lang3.ArrayUtils;

/**
 * The Class FilenameUtilTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.4.0
 */
public class FilenameUtilTemp{

    /**
     * 常用图片格式.
     * 
     * @deprecated 表述不清晰,将会重构
     */
    @Deprecated
    private static final String[] COMMON_IMAGES = { "gif", "bmp", "jpg", "png" };

    /**
     * 上传的文件是否是常用图片格式.
     * 
     * @param fileName
     *            文件名称,可以是全路径 ,也可以是 部分路径,会解析取到后缀名
     * @return 上传的文件是否是常用图片格式
     */
    public static boolean isCommonImage(String fileName){
        return isInAppointTypes(fileName, COMMON_IMAGES);
    }

    /**
     * 上传的文件是否在指定的文件类型里面.
     * 
     * @param fileName
     *            文件名称
     * @param appointTypes
     *            指定的文件类型数组
     * @return 上传的文件是否在指定的文件类型里面
     */
    // XXX 忽视大小写
    public static boolean isInAppointTypes(String fileName,String...appointTypes){
        String filePostfixName = FilenameUtil.getExtension(fileName);
        return ArrayUtils.contains(appointTypes, filePostfixName);
    }

}

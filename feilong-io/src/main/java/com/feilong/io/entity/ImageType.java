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
package com.feilong.io.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 图片类型的枚举.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.io.entity.MimeType
 * @since 1.7.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageType{

    /** JPG (Joint Photograhic Experts Group)(联合图像专家组),JPEG的文件格式一般有两种文件扩展名:.jpg和.jpeg,这两种扩展名的实质是相同的. */
    public static final String JPG  = MimeType.JPG.getExtension();

    /** JPEG (Joint Photograhic Experts Group)(联合图像专家组),JPEG的文件格式一般有两种文件扩展名:.jpg和.jpeg,这两种扩展名的实质是相同的. */
    public static final String JPEG = MimeType.JPEG.getExtension();

    /** PNG (Portable Network Graphic Format) 流式网络图形格式. */
    public static final String PNG  = MimeType.PNG.getExtension();

    /** GIF (Graphics Interchange format)(图形交换格式). */
    public static final String GIF  = MimeType.GIF.getExtension();

    /**
     * BMP Windows 位图.
     * <p>
     * 为了保证照片图像的质量,请使用 PNG 、JPEG、TIFF 文件.<br>
     * BMP文件适用于 Windows 中的墙纸 .
     * </p>
     */
    public static final String BMP  = MimeType.BMP.getExtension();

}
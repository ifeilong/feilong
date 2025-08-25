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
package com.feilong.template.extension;

import static com.feilong.core.CharsetType.UTF8;

import com.feilong.core.Validate;
import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.template.TemplateUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class TemplateFileDataUtil.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateFileDataUtil{

    /**
     * writeAndOpen.
     * 
     * <p>
     * 如果 <code>outPutFilePath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>outPutFilePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param templateFileData
     *            the velocity file data
     */
    public static void writeAndOpen(TemplateFileData templateFileData){
        String outPutFilePath = templateFileData.getOutPutFilePath();
        Validate.notBlank(outPutFilePath, "outPutFilePath can't be null/empty!");

        String templatePath = templateFileData.getTemplatePath();
        Validate.notBlank(templatePath, "templatePath can't be blank!");

        //---------------------------------------------------------------

        String content = TemplateUtil.parseTemplate(templatePath, templateFileData.getData());
        IOWriteUtil.writeStringToFile(outPutFilePath, content, UTF8);

        DesktopUtil.open(FileUtil.getParent(outPutFilePath));//和输出文件同级目录
        DesktopUtil.open(outPutFilePath);
    }
}

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
package com.feilong.component.upload;

import static com.feilong.core.date.DateUtil.nowTimestamp;
import static com.feilong.core.lang.SystemUtil.USER_HOME;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.feilong.context.fileparser.FileParser;
import com.feilong.context.fileparser.RequestFileParserDetector;
import com.feilong.io.FilenameUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

public class UploadController{

    private static final String       DIRECTORY_NAME = USER_HOME + "/feilong/upload";

    @Autowired
    private MultipartFileResolver     multipartFileResolver;

    @Autowired
    private RequestFileParserDetector requestFileParserDetector;

    //---------------------------------------------------------------

    //@RequestMapping(value = "/upload",method = { POST })
    public void upload(
                    HttpServletRequest request,
                    @SuppressWarnings("unused") HttpServletResponse response,
                    @RequestParam("importFile") MultipartFile[] multipartFiles){
        Validate.notEmpty(multipartFiles, "multipartFiles can't be empty!");

        String[] fileNames = resolverNewFileNames(multipartFiles);
        //1 上传到制定的目录 ,已备份
        List<String> filePathList = multipartFileResolver.upload(multipartFiles, DIRECTORY_NAME, fileNames);

        FileParser fileParser = requestFileParserDetector.detect(request);
        for (String filePath : filePathList){
            fileParser.parse(filePath);
        }
    }

    //---------------------------------------------------------------
    private static String[] resolverNewFileNames(MultipartFile[] multipartFiles){
        String[] fileNames = new String[multipartFiles.length];

        for (int i = 0; i < multipartFiles.length; ++i){
            MultipartFile multipartFile = multipartFiles[i];
            if (!multipartFile.isEmpty()){
                fileNames[i] = Slf4jUtil
                                .format("{}.{}", nowTimestamp() + "-" + i, FilenameUtil.getExtension(multipartFile.getOriginalFilename()));//文件后缀
            }
        }
        return fileNames;
    }

}

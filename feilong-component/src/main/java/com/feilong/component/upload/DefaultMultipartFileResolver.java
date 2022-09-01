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

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.feilong.core.Validate;
import com.feilong.io.IOWriteUtil;
import com.feilong.json.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 需要在spring相关xml里面配置
 *
 * <pre class="code">
{@code
    <bean id="multipartFileResolver" class="com.feilong.component.upload.DefaultMultipartFileResolver" />
}
 * </pre>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.4
 * @since 1.12.0 rename
 * @since 3.0.0 move from feilong spring
 */
public class DefaultMultipartFileResolver implements MultipartFileResolver{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMultipartFileResolver.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.controller.MultipartFileResolver#upload(org.springframework.web.multipart.MultipartFile, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String upload(MultipartFile multipartFile,String directoryName,String fileName){
        Validate.notNull(multipartFile, "multipartFile can't be null!");

        Validate.notBlank(directoryName, "directoryName can't be blank!");
        Validate.notBlank(fileName, "fileName can't be blank!");

        //---------------------------------------------------------------
        if (multipartFile.isEmpty()){
            LOGGER.warn("multipartFile is empty,but you want to save to directoryName:[{}],fileName:[{}]", directoryName, fileName);
            return null;
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.toString(MultipartFileUtil.getMultipartFileInfoMapForLogMap(multipartFile)));
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            return IOWriteUtil.write(inputStream, directoryName, fileName);
        }catch (IOException e){
            throw new UncheckedIOException(Slf4jUtil.format("directoryName:[{}],fileName:[{}]", directoryName, fileName), e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.multipart.MultipartFileResolver#upload(org.springframework.web.multipart.MultipartFile[],
     * java.lang.String, java.lang.String[])
     */
    @Override
    public List<String> upload(MultipartFile[] multipartFiles,String directoryName,String[] fileNames){
        Validate.notEmpty(multipartFiles, "multipartFiles can't be empty!");
        Validate.notEmpty(fileNames, "fileNames can't be empty!");

        List<String> list = newArrayList();

        for (int i = 0; i < multipartFiles.length; ++i){
            MultipartFile multipartFile = multipartFiles[i];
            if (multipartFile.isEmpty()){
                continue;
            }
            String fileName = fileNames[i];
            Validate.notBlank(fileName, "fileName can't be blank!");

            list.add(this.upload(multipartFile, directoryName, fileName));
        }

        return list;
    }
}

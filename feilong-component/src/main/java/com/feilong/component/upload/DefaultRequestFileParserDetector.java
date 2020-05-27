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

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.feilong.context.fileparser.FileParser;
import com.feilong.context.fileparser.RequestFileParserDetector;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class DefaultRequestFileParserDetector implements RequestFileParserDetector{

    @Autowired
    private final Map<String, FileParser> map               = newHashMap();

    /** 文件标识 参数名字. */
    private String                        identifyParamName = "key";
    //---------------------------------------------------------------

    @Override
    public FileParser detect(HttpServletRequest request){
        //取到标识的值
        String identifyValue = request.getParameter(identifyParamName);
        Validate.notBlank(identifyValue, "key can't be blank!");

        //基于值 探测出FileParser
        FileParser fileParser = map.get(identifyValue);
        Validate.notNull(
                        fileParser,
                        "by identifyValue:%s,can't find fileParser!Either the corresponding fileParser is not configured, or the parameter:[%s] is wrong",
                        identifyValue,
                        identifyParamName);

        return fileParser;
    }
    //---------------------------------------------------------------

    /**
     * 设置 文件标识 参数名字.
     *
     * @param fileIdentifyParamName
     *            the fileIdentifyParamName to set
     */
    public void setIdentifyParamName(String fileIdentifyParamName){
        this.identifyParamName = fileIdentifyParamName;
    }
}

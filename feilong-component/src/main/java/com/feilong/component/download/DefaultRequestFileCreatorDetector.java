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
package com.feilong.component.download;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.feilong.context.filecreator.RequestFileCreator;
import com.feilong.context.filecreator.RequestFileCreatorDetector;

/**
 * 默认基于request请求的标识参数名字来,来探测使用的{@link RequestFileCreator}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class DefaultRequestFileCreatorDetector implements RequestFileCreatorDetector{

    /** The request file creator map. */
    @Autowired
    private Map<String, RequestFileCreator> requestFileCreatorMap;

    /** 文件标识 参数名字. */
    private String                          identifyParamName = "key";

    //---------------------------------------------------------------

    /**
     * Detect.
     *
     * @param request
     *            the request
     * @return the request file creator
     */
    @Override
    public RequestFileCreator detect(HttpServletRequest request){
        //取到标识的值
        String identifyValue = request.getParameter(identifyParamName);
        Validate.notBlank(identifyValue, "key can't be blank!");

        //基于值 探测出RequestFileCreator
        RequestFileCreator requestFileCreator = requestFileCreatorMap.get(identifyValue);
        Validate.notNull(
                        requestFileCreator,
                        "by identifyValue:%s,can't find requestFileCreator!Either the corresponding requestfilecreator is not configured, or the parameter:[%s] is wrong",
                        identifyValue,
                        identifyParamName);

        return requestFileCreator;
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

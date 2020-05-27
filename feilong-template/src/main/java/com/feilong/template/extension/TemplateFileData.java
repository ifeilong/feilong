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

import java.util.Map;

/**
 * The Class TemplateFileData.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.9.2
 */
public class TemplateFileData{

    /** 模板地址. */
    private String         templatePath;
    //---------------------------------------------------------------

    /** 数据. */
    private Map<String, ?> data;

    /** 输出的文件. */
    private String         outPutFilePath;

    //---------------------------------------------------------------

    /**
     * Instantiates a new velocity file data.
     */
    public TemplateFileData(){
        super();
    }

    /**
     * Instantiates a new velocity file data.
     *
     * @param templatePath
     *            the vm path
     * @param data
     *            the data
     * @param outPutFilePath
     *            the out put file path
     */
    public TemplateFileData(String templatePath, Map<String, ?> data, String outPutFilePath){
        super();
        this.templatePath = templatePath;
        this.data = data;
        this.outPutFilePath = outPutFilePath;
    }

    //---------------------------------------------------------------

    /**
     * 获得 velocity 模板地址.
     *
     * @return the 模板地址
     */
    public String getTemplatePath(){
        return templatePath;
    }

    /**
     * 设置 velocity 模板地址.
     *
     * @param templatePath
     *            the new 模板地址
     */
    public void setTemplatePath(String templatePath){
        this.templatePath = templatePath;
    }

    //---------------------------------------------------------------

    /**
     * 获得 数据.
     *
     * @return the data
     */
    public Map<String, ?> getData(){
        return data;
    }

    /**
     * 设置 数据.
     *
     * @param data
     *            the data to set
     */
    public void setData(Map<String, ?> data){
        this.data = data;
    }

    //---------------------------------------------------------------

    /**
     * 获得 输出的文件.
     *
     * @return the outPutFilePath
     */
    public String getOutPutFilePath(){
        return outPutFilePath;
    }

    /**
     * 设置 输出的文件.
     *
     * @param outPutFilePath
     *            the outPutFilePath to set
     */
    public void setOutPutFilePath(String outPutFilePath){
        this.outPutFilePath = outPutFilePath;
    }

}

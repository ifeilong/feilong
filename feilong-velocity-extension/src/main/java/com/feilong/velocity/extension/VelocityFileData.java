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
package com.feilong.velocity.extension;

import java.util.Map;

/**
 * The Class VelocityFileData.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.9.2
 */
public class VelocityFileData{

    /** velocity 模板地址. */
    private String         vmPath;

    /** 数据. */
    private Map<String, ?> data;

    /** 输出的文件. */
    private String         outPutFilePath;

    //---------------------------------------------------------------

    /**
     * Instantiates a new velocity file data.
     */
    public VelocityFileData(){
        super();
    }

    /**
     * Instantiates a new velocity file data.
     *
     * @param vmPath
     *            the vm path
     * @param data
     *            the data
     * @param outPutFilePath
     *            the out put file path
     */
    public VelocityFileData(String vmPath, Map<String, ?> data, String outPutFilePath){
        super();
        this.vmPath = vmPath;
        this.data = data;
        this.outPutFilePath = outPutFilePath;
    }

    //---------------------------------------------------------------

    /**
     * 获得 velocity 模板地址.
     *
     * @return the vmPath
     */
    public String getVmPath(){
        return vmPath;
    }

    /**
     * 设置 velocity 模板地址.
     *
     * @param vmPath
     *            the vmPath to set
     */
    public void setVmPath(String vmPath){
        this.vmPath = vmPath;
    }

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

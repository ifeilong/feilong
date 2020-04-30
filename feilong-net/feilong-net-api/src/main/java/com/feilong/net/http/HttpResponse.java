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
package com.feilong.net.http;

import java.io.Serializable;
import java.util.Map;

/**
 * 得到的 http response 结果信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class HttpResponse implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 1116415641000552013L;

    //---------------------------------------------------------------

    /** 响应的状态码,比如 200 , 404. */
    private int                 statusCode;

    /** 响应的结果字符串. */
    private String              resultString;

    /**
     * 响应头信息.
     * 
     * @since 1.12.5 change to map
     */
    private Map<String, String> headerMap;

    /** 耗时时间,精确到<span style="color:red">毫秒</span>. */
    private long                useTime;

    //---------------------------------------------------------------

    /**
     * 获得 耗时时间,<span style="color:red">毫秒</span>.
     *
     * @return the useTime
     */
    public long getUseTime(){
        return useTime;
    }

    /**
     * 设置 耗时时间,<span style="color:red">毫秒</span>.
     *
     * @param useTime
     *            the useTime to set
     */
    public void setUseTime(long useTime){
        this.useTime = useTime;
    }

    /**
     * 获得 响应的状态码.
     *
     * @return the statusCode
     */
    public int getStatusCode(){
        return statusCode;
    }

    /**
     * 设置 响应的状态码.
     *
     * @param statusCode
     *            the statusCode to set
     */
    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }

    /**
     * 获得 响应的结果字符串.
     *
     * @return the resultString
     */
    public String getResultString(){
        return resultString;
    }

    /**
     * 设置 响应的结果字符串.
     *
     * @param resultString
     *            the resultString to set
     */
    public void setResultString(String resultString){
        this.resultString = resultString;
    }

    /**
     * 获得 响应头信息.
     *
     * @return the headerMap
     * @since 1.12.5
     */
    public Map<String, String> getHeaderMap(){
        return headerMap;
    }

    /**
     * 设置 响应头信息.
     *
     * @param headerMap
     *            the headerMap to set
     * @since 1.12.5
     */
    public void setHeaderMap(Map<String, String> headerMap){
        this.headerMap = headerMap;
    }
}

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
package com.feilong.core.entity;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

public class DangaMemCachedNoAliasConfig{

    /** The serverlist. */
    private String[] serverList;

    /** The min connection. */
    private Integer  minConnection;

    private Integer  maxConnection;

    /** 关闭套接字缓存. */
    private Boolean  nagle;

    //---------------------------------------------------------------

    /**
     * @return the serverList
     */
    public String[] getServerList(){
        return serverList;
    }

    /**
     * @param serverList
     *            the serverList to set
     */
    public void setServerList(String[] serverList){
        this.serverList = serverList;
    }

    /**
     * @return the minConnection
     */
    public Integer getMinConnection(){
        return minConnection;
    }

    /**
     * @param minConnection
     *            the minConnection to set
     */
    public void setMinConnection(Integer minConnection){
        this.minConnection = minConnection;
    }

    /**
     * @return the nagle
     */
    public Boolean getNagle(){
        return nagle;
    }

    /**
     * @param nagle
     *            the nagle to set
     */
    public void setNagle(Boolean nagle){
        this.nagle = nagle;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * @return the maxConnection
     */
    public Integer getMaxConnection(){
        return maxConnection;
    }

    /**
     * @param maxConnection
     *            the maxConnection to set
     */
    public void setMaxConnection(Integer maxConnection){
        this.maxConnection = maxConnection;
    }

}

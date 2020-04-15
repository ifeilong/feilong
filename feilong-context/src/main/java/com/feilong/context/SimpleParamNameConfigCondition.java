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
package com.feilong.context;

/**
 * 简单的基于参数名字配置的条件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.14.3
 */
public class SimpleParamNameConfigCondition implements Condition{

    /** The simple param name value loader. */
    private SimpleParamNameValueLoader<Boolean> simpleParamNameValueLoader;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.scheduling.quartz.Condition#is()
     */
    @Override
    public boolean canRun(){
        return simpleParamNameValueLoader.load();
    }

    //---------------------------------------------------------------

    /**
     * Sets the simple param name value loader.
     *
     * @param simpleParamNameValueLoader
     *            the simpleParamNameValueLoader to set
     */
    public void setSimpleParamNameValueLoader(SimpleParamNameValueLoader<Boolean> simpleParamNameValueLoader){
        this.simpleParamNameValueLoader = simpleParamNameValueLoader;
    }

}

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
package com.feilong.tools.log;

/**
 * The Class ProcessLogParamEntity.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.0
 */
public class ProcessLogParamEntity{

    /** 当前执行到哪个位置. */
    private Number current;

    /** 需要执行的总数. */
    private Number all;

    //---------------------------------------------------------------
    /** 当前执行的开始时间. */
    private Long   currentBeginTimeMillis;

    /** 所有开始执行的开始时间. */
    private Long   allBeginTimeMillis;

    //---------------------------------------------------------------

    /**
     * Instantiates a new process log param entity.
     */
    public ProcessLogParamEntity(){
        super();
    }

    /**
     * Instantiates a new process log param entity.
     *
     * @param current
     *            the current
     * @param all
     *            the all
     * @param currentBeginTimeMillis
     *            the current begin time millis
     * @param allBeginTimeMillis
     *            the all begin time millis
     */
    public ProcessLogParamEntity(Number current, Number all, Long currentBeginTimeMillis, Long allBeginTimeMillis){
        super();
        this.current = current;
        this.all = all;
        this.currentBeginTimeMillis = currentBeginTimeMillis;
        this.allBeginTimeMillis = allBeginTimeMillis;
    }

    //---------------------------------------------------------------

    /**
     * 获得 当前执行到哪个位置.
     *
     * @return the current
     */
    public Number getCurrent(){
        return current;
    }

    /**
     * 设置 当前执行到哪个位置.
     *
     * @param current
     *            the current to set
     */
    public void setCurrent(Number current){
        this.current = current;
    }

    /**
     * 获得 需要执行的总数.
     *
     * @return the all
     */
    public Number getAll(){
        return all;
    }

    /**
     * 设置 需要执行的总数.
     *
     * @param all
     *            the all to set
     */
    public void setAll(Number all){
        this.all = all;
    }

    /**
     * 获得 当前执行的开始时间.
     *
     * @return the currentBeginTimeMillis
     */
    public Long getCurrentBeginTimeMillis(){
        return currentBeginTimeMillis;
    }

    /**
     * 设置 当前执行的开始时间.
     *
     * @param currentBeginTimeMillis
     *            the currentBeginTimeMillis to set
     */
    public void setCurrentBeginTimeMillis(Long currentBeginTimeMillis){
        this.currentBeginTimeMillis = currentBeginTimeMillis;
    }

    /**
     * 获得 所有开始执行的开始时间.
     *
     * @return the allBeginTimeMillis
     */
    public Long getAllBeginTimeMillis(){
        return allBeginTimeMillis;
    }

    /**
     * 设置 所有开始执行的开始时间.
     *
     * @param allBeginTimeMillis
     *            the allBeginTimeMillis to set
     */
    public void setAllBeginTimeMillis(Long allBeginTimeMillis){
        this.allBeginTimeMillis = allBeginTimeMillis;
    }

}

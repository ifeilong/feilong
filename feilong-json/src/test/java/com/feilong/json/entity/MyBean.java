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
package com.feilong.json.entity;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import com.feilong.store.member.Person;

/**
 * The Class MyBean.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0 Jan 26, 2013 2:43:19 PM
 */
public class MyBean{

    /** The id. */
    private Long         id;

    private Person       person;

    /** The data. */
    private List<Object> data = newArrayList();

    //---------------------------------------------------------------

    /**
     * 获得 data.
     *
     * @return the data
     */
    public List<Object> getData(){
        return data;
    }

    /**
     * 设置 data.
     *
     * @param data
     *            the data to set
     */
    public void setData(List<Object> data){
        this.data = data;
    }

    /**
     * 获得 id.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 id.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * @param person
     *            the person to set
     */
    public void setPerson(Person person){
        this.person = person;
    }

    /**
     * @return the person
     */
    public Person getPerson(){
        return person;
    }

}

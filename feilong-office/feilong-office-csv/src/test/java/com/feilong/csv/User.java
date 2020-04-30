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
package com.feilong.csv;

import java.io.Serializable;

import com.feilong.csv.entity.CsvColumn;

/**
 * The Class User.
 */
public class User implements Serializable{

    private static final long serialVersionUID = -5082905765755960876L;

    /** The username. */
    @CsvColumn(name = "usern ame ",order = 5)
    private String            username;

    /** The name. */
    @CsvColumn(name = "名字",order = 16)
    private String            name;

    /** The age. */
    @CsvColumn(name = "a ge",order = 10)
    private int               age;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param username
     *            the username
     * @param name
     *            the name
     * @param age
     *            the age
     */
    public User(String username, String name, int age){
        this.username = username;
        this.name = name;
        this.age = age;
    }

    /**
     * 获得 username.
     *
     * @return the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * 设置 username.
     *
     * @param username
     *            the username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * 获得 name.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * 设置 name.
     *
     * @param name
     *            the name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 获得 age.
     *
     * @return the age
     */
    public int getAge(){
        return age;
    }

    /**
     * 设置 age.
     *
     * @param age
     *            the age
     */
    public void setAge(int age){
        this.age = age;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("username : ").append(this.getUsername());
        sb.append(", name : ").append(this.getName());
        sb.append(", age : ").append(this.getAge());
        return sb.toString();
    }

}
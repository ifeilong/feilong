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
package com.feilong.net.bot.message;

import java.util.List;

/**
 * The Class MessageParams.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
public class MessageParams{

    /** 消息会话列表中展示的标题，非消息体的标题。. */
    private String       title;

    /** 被@人的手机号。 注意在text内容里要有@人的手机号. */
    private String[]     atMobiles;

    /**
     * 被@人的用户userid
     * <p>
     * 发送消息是基于userid，这个userid跟钉钉账号还不一样，是钉钉开发者平台的userid。<br>
     * 可以通过钉钉账号的信息来获取，这里使用的是get_userid用手机号获取方式；
     * </p>
     * .
     */
    private List<String> atUserIds;

    //---------------------------------------------------------------

    /** 是否@所有人. */
    private boolean      isAtAll;

    //---------------------------------------------------------------

    /**
     * 获得 被@人的手机号。 注意在text内容里要有@人的手机号.
     *
     * @return the atMobiles
     */
    public String[] getAtMobiles(){
        return atMobiles;
    }

    /**
     * 设置 被@人的手机号。 注意在text内容里要有@人的手机号.
     *
     * @param atMobiles
     *            the atMobiles to set
     */
    public void setAtMobiles(String...atMobiles){
        this.atMobiles = atMobiles;
    }

    /**
     * 获得 被@人的用户userid
     * <p>
     * 发送消息是基于userid，这个userid跟钉钉账号还不一样，是钉钉开发者平台的userid。<br>
     * 可以通过钉钉账号的信息来获取，这里使用的是get_userid用手机号获取方式；
     * </p>
     * .
     *
     * @return the atUserIds
     */
    public List<String> getAtUserIds(){
        return atUserIds;
    }

    /**
     * 设置 被@人的用户userid
     * <p>
     * 发送消息是基于userid，这个userid跟钉钉账号还不一样，是钉钉开发者平台的userid。<br>
     * 可以通过钉钉账号的信息来获取，这里使用的是get_userid用手机号获取方式；
     * </p>
     * .
     *
     * @param atUserIds
     *            the atUserIds to set
     */
    public void setAtUserIds(List<String> atUserIds){
        this.atUserIds = atUserIds;
    }

    /**
     * 获得 是否@所有人.
     *
     * @return the isAtAll
     */
    public boolean getIsAtAll(){
        return isAtAll;
    }

    /**
     * 设置 是否@所有人.
     *
     * @param isAtAll
     *            the isAtAll to set
     */
    public void setIsAtAll(boolean isAtAll){
        this.isAtAll = isAtAll;
    }

    /**
     * 获得 消息会话列表中展示的标题，非消息体的标题。.
     *
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置 消息会话列表中展示的标题，非消息体的标题。.
     *
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

}

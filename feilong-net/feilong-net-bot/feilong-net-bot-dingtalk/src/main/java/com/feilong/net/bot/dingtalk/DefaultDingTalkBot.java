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
package com.feilong.net.bot.dingtalk;

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import com.feilong.core.Validate;
import com.feilong.net.bot.AbstractBot;
import com.feilong.net.bot.dingtalk.message.At;
import com.feilong.net.bot.dingtalk.message.markdown.DingtalkMarkdownMessage;
import com.feilong.net.bot.dingtalk.message.markdown.Markdown;
import com.feilong.net.bot.message.MessageParams;

/**
 * 默认的钉钉机器人.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href=
 *      "https://developers.dingtalk.com/document/robots/custom-robot-access-1?spm=ding_open_doc.document.0.0.6d9d10afLWgSfH#topic-2026027">如何配置群机器人？</a>
 * 
 * @since 3.1.0
 */
public class DefaultDingTalkBot extends AbstractBot implements DingTalkBot{

    /** accessToken. */
    private String accessToken;

    /**
     * 自定义加签可选秘钥.
     * 
     * <p>
     * 如果设置了, 将使用 钉钉加签规则: 把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64 encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）
     * </p>
     * 
     * @see <a href="https://open.dingtalk.com/document/robots/customize-robot-security-settings">自定义机器人安全设置</a>
     */
    private String secret;

    //---------------------------------------------------------------

    /** markdown默认title. */
    private String defaultTitle = "feilongDingTalk";

    //---------------------------------------------------------------

    /**
     * Instantiates a new default wxwork bot.
     */
    public DefaultDingTalkBot(){
        super();
    }

    /**
     * Instantiates a new default wxwork bot.
     *
     * @param accessToken
     *            the key
     */
    public DefaultDingTalkBot(String accessToken){
        super();
        this.accessToken = accessToken;
    }

    /**
     * Instantiates a new default ding talk bot.
     *
     * @param accessToken
     *            the key
     * @param secret
     *            the secret
     */
    public DefaultDingTalkBot(String accessToken, String secret){
        super();
        this.accessToken = accessToken;
        this.secret = secret;
    }
    //---------------------------------------------------------------

    /**
     * Send message.
     *
     * @param title
     *            the title
     * @param content
     *            the content
     * @param atMobiles
     *            the at mobiles
     * @return true, if successful
     * @deprecated pls use {@link #sendMessage(String, com.feilong.net.bot.message.MessageParams)}
     */
    @Deprecated
    @Override
    public boolean sendMessage(String title,String content,String...atMobiles){
        MessageParams messageParams = new MessageParams();
        messageParams.setTitle(title);
        messageParams.setAtMobiles(atMobiles);
        return sendMessage(content, messageParams);
    }

    @Override
    protected boolean doSendMessage(String logPrefix,String content,MessageParams messageParams){
        Validate.notBlank(content, "content can't be blank!");
        Validate.notNull(messageParams, "messageParams can't be null!");

        Markdown markdown = new Markdown(defaultIfNullOrEmpty(messageParams.getTitle(), defaultTitle), content);
        DingtalkMarkdownMessage botMessage = new DingtalkMarkdownMessage(markdown);
        botMessage.setAt(new At(messageParams.getAtMobiles(), messageParams.getAtUserIds(), messageParams.getIsAtAll()));

        return DingTalkPushUtil.pushMessage(botMessage, accessToken, secret);
    }

    //---------------------------------------------------------------

    /**
     * Sets the key.
     *
     * @param accessToken
     *            the key to set
     */
    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    /**
     * 自定义加签可选秘钥.
     * 
     * <p>
     * 如果设置了, 将使用 钉钉加签规则: 把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64 encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）
     * </p>
     *
     * @param secret
     *            the new 自定义加签可选秘钥
     * @see <a href="https://open.dingtalk.com/document/robots/customize-robot-security-settings">自定义机器人安全设置</a>
     */
    public void setSecret(String secret){
        this.secret = secret;
    }

    /**
     * 设置 markdown默认title.
     *
     * @param defaultTitle
     *            the new markdown默认title
     */
    public void setDefaultTitle(String defaultTitle){
        this.defaultTitle = defaultTitle;
    }

    //---------------------------------------------------------------

    @Override
    public String getKey(){
        return accessToken;
    }

}

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

/**
 * 用来构造{@link DingTalkBot}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.3.8
 */
public class DingTalkBotBuilder{

    /**
     * Builds the async catch exception.
     *
     * @param accessToken
     *            the access token
     * @param secret
     *            the secret
     * @param defaultTitle
     *            the default title
     * @return the ding talk bot
     */
    public static DingTalkBot newAsyncCatchExceptionDingTalkBot(String accessToken,String secret,String defaultTitle){
        DefaultDingTalkBot defaultDingTalkBot = new DefaultDingTalkBot();
        defaultDingTalkBot.setAccessToken(accessToken);
        defaultDingTalkBot.setDefaultTitle(defaultTitle);
        defaultDingTalkBot.setSecret(secret);
        defaultDingTalkBot.setIsAsync(true);
        defaultDingTalkBot.setIsCatchException(true);
        return defaultDingTalkBot;
    }
}

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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_MINUTE;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.net.http.HttpClientUtil.getResponseBodyAsString;
import static com.feilong.net.http.HttpMethodType.POST;

import java.net.URLEncoder;
import java.util.Map;

import com.feilong.core.Validate;
import com.feilong.io.entity.MimeType;
import com.feilong.json.JsonUtil;
import com.feilong.lib.codec.digest.HmacAlgorithms;
import com.feilong.lib.codec.digest.HmacUtils;
import com.feilong.net.bot.message.BotMessage;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.security.Base64Util;
import com.feilong.security.EncryptionException;

/**
 * 钉钉机器人推送工具类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href=
 *      "https://developers.dingtalk.com/document/robots/custom-robot-access-1?spm=ding_open_doc.document.0.0.6d9d10afLWgSfH#topic-2026027">如何配置群机器人？</a>
 * @since 4.0.8
 */
public class DingTalkPushUtil{

    /** The Constant BOT_WEBHOOK_URL. */
    private static final String BOT_WEBHOOK_URL = "https://oapi.dingtalk.com/robot/send";

    //---------------------------------------------------------------
    /**
     * 发起POST请求时，必须将字符集编码设置成UTF-8
     * 
     * 每个机器人每分钟最多发送20条。
     * 消息发送太频繁会严重影响群成员的使用体验，
     * 大量发消息的场景 (譬如系统监控报警) 可以将这些信息进行整合，通过markdown消息以摘要的形式发送到群里。
     * 
     * 当前自定义机器人支持文本 (text)、
     * 链接 (link)、
     * markdown(markdown)、
     * ActionCard、
     * FeedCard消息类型，
     * 
     * 请根据自己的使用场景选择合适的消息类型，达到最好的展示样式。
     * 详情参考：消息类型及数据格式。
     * 
     * 自定义机器人发送消息时，可以通过手机号码指定“被@人列表”。
     * 在“被@人列表”里面的人员收到该消息时，会有@消息提醒。免打扰会话仍然通知提醒，首屏出现“有人@你”。.
     *
     * @param <T>
     *            the generic type
     * @param botMessage
     *            the bot message
     * @param accessToken
     *            the access token
     * @param secret
     *            自定义加签可选秘钥.
     * 
     *            <p>
     *            如果设置了, 将使用 钉钉加签规则: 把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64
     *            encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）
     *            </p>
     * @return the boolean
     */
    public static <T extends BotMessage> Boolean pushMessage(T botMessage,String accessToken,String secret){
        String msgtype = botMessage.getMsgtype();
        Validate.notBlank(msgtype, "msgtype can't be blank!");

        //---------------------------------------------------------------
        HttpRequest httpRequest = new HttpRequest(BOT_WEBHOOK_URL, POST);

        Map<String, String> map = toMap("access_token", accessToken);

        if (isNotNullOrEmpty(secret)){
            Long timestamp = System.currentTimeMillis();
            map.put("timestamp", "" + timestamp);
            map.put("sign", buildSign(timestamp, secret));
        }

        //---------------------------------------------------------------
        httpRequest.setParamMap(map);
        httpRequest.setRequestBody(JsonUtil.toString(botMessage));

        //必须是 json
        httpRequest.setHeaderMap(toMap("Content-Type", MimeType.JSON.getMime()));

        //---------------------------------------------------------------

        int connectTimeout = 2 * MILLISECOND_PER_MINUTE;
        String json = getResponseBodyAsString(httpRequest, new ConnectionConfig(connectTimeout));
        DingtalkResponse dingtalkResponse = JsonUtil.toBean(json, DingtalkResponse.class);
        return dingtalkResponse.getIsSuccess();
    }

    /**
     * 把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64 encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）.
     *
     * @param timestamp
     *            当前时间戳，单位是毫秒，与请求调用时间误差不能超过1小时
     * @param secret
     *            密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符串
     * @return the string
     * @since 3.1.0
     */
    private static String buildSign(Long timestamp,String secret){
        try{
            HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);

            byte[] signData = hmacUtils.hmac(timestamp + "\n" + secret);

            return URLEncoder.encode(Base64Util.encodeBase64(signData), UTF8);
        }catch (Exception e){
            throw new EncryptionException(e);
        }
    }
}

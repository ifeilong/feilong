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
package com.feilong.context.invoker;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.Rebuilder;
import com.feilong.context.format.JsonStringFormatter;
import com.feilong.context.format.StringFormatter;
import com.feilong.context.format.XMLStringFormatter;
import com.feilong.json.JsonUtil;

/**
 * 抽象的响应结果字符串构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 */
public abstract class AbstractResponseStringBuilder<T> implements ResponseStringBuilder<T>{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResponseStringBuilder.class);

    //---------------------------------------------------------------

    /**
     * 字符串格式化,用来简化 {@link StringFormatter} 配置.
     * 
     * <p>
     * 优先级 stringFormatter{@code  > }format
     * </p>
     * 
     * @since 2.1.0
     */
    private String              format;

    /** 字符串格式化器. */
    private StringFormatter     stringFormatter;

    //---------------------------------------------------------------
    /**
     * 再加工器.
     * 
     * <p>
     * <span style="color:red">一般情况下用不到</span>
     * </p>
     * 
     * <p>
     * 支持在获得 responseString 之后,再次加工
     * </p>
     * 
     * <p>
     * 典型应用,cc 视频, 返回
     * 
     * <pre>
    {"video":{"copy":[{"quality":"30","playurl":"https://cm15-c110-2.play.bokecc.com/flvs/ca/Qx4tq/u1TjiRB1VS-30.mp4?t=1560569092&key=D5E65894227B921ACDC77BD3B82763B1&tpl=10"}]}}
     * </pre>
     * 
     * <br>
     * 这时候, 我只想得到 playurl 的值
     * </p>
     * 
     * @since 1.14.0
     */
    private Rebuilder<String>   responseStringRebuilder;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.netpay.handler.close.GatewayCloseResponseBuilder#build(com.feilong.netpay.handler.close.CloseRequest)
     */
    @Override
    public String build(T request){
        String responseString = handler(request);

        //---------------------------------------------------------------
        //since 1.14.0
        if (null == responseStringRebuilder){
            logMessage(request, responseString);
            return responseString;
        }

        //---------------------------------------------------------------
        String rebuildResult = responseStringRebuilder.rebuild(responseString);
        logMessage(request, responseString, responseStringRebuilder, rebuildResult);
        return rebuildResult;
    }

    //---------------------------------------------------------------

    /**
     * Handler.
     *
     * @param request
     *            the request
     * @return the string
     * @since 1.11.3
     */
    protected abstract String handler(T request);

    //---------------------------------------------------------------

    /**
     * Post build.
     *
     * @param request
     *            the request
     * @param responseString
     *            the response string
     * @since 1.11.2
     * @since 1.14.0 change private
     */
    private void logMessage(T request,String responseString){
        if (LOGGER.isDebugEnabled()){
            String response = formatResponse(responseString);
            LOGGER.debug("request:[{}],responseString:[{}]", JsonUtil.format(request), response);
        }
    }

    /**
     * Log message.
     *
     * @param request
     *            the request
     * @param responseString
     *            the response string
     * @param rebuilder
     *            the rebuilder
     * @param rebuildResult
     *            the rebuild result
     * @since 1.14.0
     */
    private void logMessage(T request,String responseString,Rebuilder<String> rebuilder,String rebuildResult){
        if (LOGGER.isInfoEnabled()){
            String response = formatResponse(responseString);
            LOGGER.info(
                            "request:[{}],responseString:[{}],after use rebuilder:[{}],will return:[{}]",
                            JsonUtil.format(request),
                            response,
                            rebuilder.getClass().getCanonicalName(),
                            rebuildResult);
        }
    }

    //---------------------------------------------------------------

    /**
     * Format response.
     *
     * @param responseString
     *            the response string
     * @return the string
     * @since 1.14.0
     */
    private String formatResponse(String responseString){
        if (null != stringFormatter){
            return stringFormatter.format(responseString);
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(format)){
            String formatValue = format.trim();
            if (formatValue.equalsIgnoreCase("xml")){
                return XMLStringFormatter.INSTANCE.format(responseString);
            }
            if (formatValue.equalsIgnoreCase("json")){
                return JsonStringFormatter.INSTANCE.format(responseString);
            }
        }

        return responseString;
    }

    //---------------------------------------------------------------

    /**
     * 设置 字符串格式化器.
     *
     * @param stringFormatter
     *            the stringFormatter to set
     */
    public void setStringFormatter(StringFormatter stringFormatter){
        this.stringFormatter = stringFormatter;
    }

    /**
     * 再加工器.
     * 
     * <p>
     * <span style="color:red">一般情况下用不到</span>
     * </p>
     * 
     * <p>
     * 支持在获得 responseString 之后,再次加工
     * </p>
     * 
     * 典型应用,cc 视频, 返回
     * 
     * <pre>
     *     {"video":{"copy":[{"quality":"30","playurl":"https://cm15-c110-2.play.bokecc.com/flvs/ca/Qx4tq/u1TjiRB1VS-30.mp4?t=1560569092&key=D5E65894227B921ACDC77BD3B82763B1&tpl=10"}]}}
     * </pre>
     * 
     * <br>
     * 这时候, 我只想得到 playurl 的值
     *
     * @param responseStringRebuilder
     *            the new 再加工器
     * @since 1.14.0
     */
    public void setResponseStringRebuilder(Rebuilder<String> responseStringRebuilder){
        this.responseStringRebuilder = responseStringRebuilder;
    }

    /**
     * 字符串格式化,用来简化 {@link StringFormatter} 配置.
     * 
     * <p>
     * 优先级 stringFormatter{@code  > }format
     * </p>
     *
     * @param format
     *            the format to set
     */
    public void setFormat(String format){
        this.format = format;
    }
}

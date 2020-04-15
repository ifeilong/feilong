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
package com.feilong.taglib.display.httpconcat.builder;

import static com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.text.MessageFormatUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.taglib.display.httpconcat.handler.ConcatLinkResolver;

/**
 * 结果构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 * @since 1.11.1 rename
 */
public class ResultBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultBuilder.class);

    /** Don't let anyone instantiate this class. */
    private ResultBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 构造content.
     * 
     * @param itemSrcList
     *            the item src list
     * @param httpConcatParam
     *            封装解析http concat 用到的参数.
     * @return the string
     * @since 1.4.1
     * @since 1.11.1 rename
     * @since 1.12.6 remove httpConcatGlobalConfig param
     */
    public static String build(List<String> itemSrcList,HttpConcatParam httpConcatParam){
        // 下面的解析均基于standardHttpConcatParam来操作,httpConcatParam只做入参判断,数据转换,以及cache存取
        HttpConcatParam standardHttpConcatParam = HttpConcatParamBuilder.standardHttpConcatParam(itemSrcList, httpConcatParam);

        //---------------------------------------------------------------
        boolean concatSupport = concatSupport(httpConcatParam);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "after standard info:[{}],itemSrcList:[{}],concatSupport:[{}]",
                            JsonUtil.format(standardHttpConcatParam),
                            JsonUtil.format(itemSrcList),
                            concatSupport);
        }

        //---------------------------------------------------------------
        String template = TemplateFactory.getTemplate(standardHttpConcatParam.getType());
        if (concatSupport){ // concat
            return handlerConcat(itemSrcList, template, standardHttpConcatParam);
        }
        return handlerNoConcat(itemSrcList, template, standardHttpConcatParam);
    }

    //---------------------------------------------------------------

    /**
     * 拼接.
     *
     * @param itemSrcList
     *            the item src list
     * @param template
     *            the template
     * @param standardHttpConcatParam
     *            the standard http concat param
     * @return the string
     * @since 1.11.1
     */
    private static String handlerConcat(List<String> itemSrcList,String template,HttpConcatParam standardHttpConcatParam){
        Integer autoPartitionSize = GLOBAL_CONFIG.getAutoPartitionSize();
        //不需要分片
        if (null == autoPartitionSize || itemSrcList.size() <= autoPartitionSize){
            return MessageFormatUtil.format(template, ConcatLinkResolver.resolver(itemSrcList, standardHttpConcatParam));
        }

        //---------------------------------------------------------------
        //since 1.12.6
        //将 list 分成 N 份
        List<List<String>> groupList = ListUtils.partition(itemSrcList, autoPartitionSize);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groupList.size(); ++i){
            sb.append("<!-- HttpConcatTag,auto partition [" + (i + 1) + "] -->");
            sb.append(lineSeparator());

            sb.append(MessageFormatUtil.format(template, ConcatLinkResolver.resolver(groupList.get(i), standardHttpConcatParam)));
            sb.append(lineSeparator());
        }
        return sb.toString();
    }

    /**
     * 不拼接.
     *
     * @param itemSrcList
     *            the item src list
     * @param template
     *            the template
     * @param standardHttpConcatParam
     *            the standard http concat param
     * @return the string
     * @since 1.11.1
     */
    private static String handlerNoConcat(List<String> itemSrcList,String template,HttpConcatParam standardHttpConcatParam){
        // 本地开发环境支持的.
        StringBuilder sb = new StringBuilder();
        for (String itemSrc : itemSrcList){
            sb.append(MessageFormatUtil.format(template, ConcatLinkResolver.resolverNoConcatLink(itemSrc, standardHttpConcatParam)));
        }
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Concat support.
     *
     * @param httpConcatParam
     *            the http concat param
     * @return true, if successful
     * @since 1.11.1
     */
    private static boolean concatSupport(HttpConcatParam httpConcatParam){
        return defaultIfNull(
                        httpConcatParam.getHttpConcatSupport(), //
                        BooleanUtils.toBoolean(GLOBAL_CONFIG.getHttpConcatSupport()));
    }
}

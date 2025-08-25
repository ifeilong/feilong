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

import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.util.CollectionsUtil.partition;
import static com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;
import static java.lang.System.lineSeparator;

import java.util.List;

import com.feilong.core.text.MessageFormatUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.BooleanUtils;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.taglib.display.httpconcat.handler.ConcatLinkResolver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 结果构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 * @since 1.11.1 rename
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultBuilder{

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
        if (log.isDebugEnabled()){
            log.debug(
                            "after standard info:[{}],itemSrcList:[{}],concatSupport:[{}]",
                            JsonUtil.toString(standardHttpConcatParam),
                            JsonUtil.toString(itemSrcList),
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
        List<List<String>> groupList = partition(itemSrcList, autoPartitionSize);

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

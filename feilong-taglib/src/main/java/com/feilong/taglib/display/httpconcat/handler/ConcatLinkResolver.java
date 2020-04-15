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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ToStringConfig.IGNORE_NULL_OR_EMPTY_CONFIG;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * 拼接链接.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class ConcatLinkResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcatLinkResolver.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ConcatLinkResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得合并的链接.
     * 
     * <p>
     * 去重,删空等操作,已经在 {@link ItemSrcListResolver} 做过了
     * </p>
     *
     * @param itemSrcList
     *            the item src list
     * @param standardHttpConcatParam
     *            the http concat param
     * @return 如果 <code>itemSrcList size==1</code>,那么直接渲染成普通的链接<br>
     *         <code>itemSrcList</code>中有null或者empty元素,那么在渲染的时候,会忽略<br>
     * @see com.feilong.taglib.display.httpconcat.handler.ItemSrcListResolver
     * @since 1.11.1 change param order
     */
    public static String resolver(List<String> itemSrcList,HttpConcatParam standardHttpConcatParam){
        // 只有一条,输出原生字符串
        int itemSrcListSize = itemSrcList.size();
        if (itemSrcListSize == 1){
            String itemSrc = itemSrcList.get(0);
            LOGGER.debug("itemSrcList:[{}], size==1,will generate primary [{}].", itemSrc, standardHttpConcatParam.getType());
            return resolver(itemSrc, standardHttpConcatParam);
        }

        //---------------------------------------------------------------
        return resolver("??" + ConvertUtil.toString(itemSrcList, IGNORE_NULL_OR_EMPTY_CONFIG), standardHttpConcatParam);
    }

    //---------------------------------------------------------------

    /**
     * 获得不需要 Concat 的连接.
     * 
     * @param itemSrc
     *            the src
     * @param standardHttpConcatParam
     *            the http concat param
     * @return the string
     * @since 1.11.1 rename
     */
    public static String resolverNoConcatLink(String itemSrc,HttpConcatParam standardHttpConcatParam){
        return resolver(itemSrc, standardHttpConcatParam);
    }

    //---------------------------------------------------------------

    /**
     * Resolver.
     *
     * @param appendContent
     *            the append content
     * @param standardHttpConcatParam
     *            the http concat param
     * @return the string
     */
    private static String resolver(String appendContent,HttpConcatParam standardHttpConcatParam){
        StringBuilder sb = new StringBuilder();
        sb.append(standardHttpConcatParam.getDomain());
        sb.append(standardHttpConcatParam.getRoot());

        //---------------------------------------------------------------

        sb.append(appendContent);

        //---------------------------------------------------------------
        appendVersion(standardHttpConcatParam.getVersion(), sb);
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * 追加版本号.
     * 
     * @param version
     *            the version
     * @param sb
     *            the sb
     */
    private static void appendVersion(String version,StringBuilder sb){
        if (isNotNullOrEmpty(version)){
            sb.append("?");
            sb.append(version);
        }else{
            LOGGER.warn("HttpConcatParam version isNullOrEmpty,suggest you should set version value,StringBuilder info:[{}]", sb);
        }
    }
}

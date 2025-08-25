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
package com.feilong.taglib.display.pager;

import static com.feilong.core.lang.StringUtil.EMPTY;

import java.util.List;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.taglib.display.SimpleTagParamCacheManager;
import com.feilong.taglib.display.pager.command.Pager;
import com.feilong.taglib.display.pager.command.PagerAndContent;
import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;
import com.feilong.taglib.display.pager.command.PagerUrlTemplate;
import com.feilong.taglib.display.pager.command.PagerVMParam;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 分页构造器.
 * 
 * <p>
 * 该类主要是将url相关数据转成vm需要的数据,并解析成字符串返回.
 * </p>
 * 
 * <h3>日志:</h3>
 * 
 * <blockquote>
 * <p>
 * 内部会分别对入参 {@link PagerParams} 和构造vm参数,记录 <b>debug</b> 级别的log,<br>
 * 如果不需要care这部分log,可以在日志配置文件中配置,将log输出的级别调高
 * </p>
 * 
 * <h4>log4j.xml</h4>
 * 
 * <pre class="code">
 * {@code
 *  <category name="com.feilong.taglib.display.pager.PagerBuilder">
 *      <priority value="info" />
 *  </category>
 * }
 * </pre>
 * 
 * <h4>logback.xml</h4>
 * 
 * <pre class="code">
 * {@code
 *      <logger name="com.feilong.taglib.display.pager.PagerBuilder" level="info" />
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>VM中支持国际化:</h3>
 * 
 * <blockquote>
 * <p>
 * VM中支持国际化,您可以见国际化需要的参数 设置到 {@link PagerConstants#I18N_FEILONG_PAGER} 配置文件中, <br>
 * 程序会解析该文件所有的key/values到 {@link PagerConstants#VM_KEY_I18NMAP} 变量,您可以在VM中直接使用
 * </p>
 * </blockquote>
 * 
 * <h3>缓存:</h3>
 * 
 * <blockquote>
 * <p>
 * 作为vm解析,如果是官方商城常用页面渲染,在大流量的场景下,其实开销也是不小的,<br>
 * 基于如果传入的参数 {@link PagerParams}是一样的 {@link PagerParams#hashCode()} &&{@link PagerParams#equals(Object)},那么分页结果也应该是相同的<br>
 * 因此,如果对性能有很高的要求的话,可以使用cache
 * </p>
 * 
 * <h4>缓存清理:</h4>
 * 
 * <blockquote>
 * <p>
 * 当vm模板内容更改,需要清理缓存,由于pagerCache 是基于JVM内存级的,因此重启应用即会生效
 * </p>
 * </blockquote>
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see PagerConstants
 * @see PagerParams
 * @see PagerUrlTemplate
 * @see PagerVMParam
 * @since 1.0.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PagerBuilder{

    /**
     * 通常用于ajax分页.
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @param itemList
     *            the item list
     * @return the pager and content
     * @see com.feilong.taglib.display.pager.command.PagerType#NO_REDIRECT
     * @since 1.4.0
     */
    public static <T> PagerAndContent<T> buildPagerAndContent(PagerParams pagerParams,List<T> itemList){
        Pager<T> pager = PagerCacheContentBuilder.buildPager(pagerParams);
        pager.setItemList(itemList);

        return new PagerAndContent<>(pager, buildContent(pagerParams));
    }

    //---------------------------------------------------------------

    /**
     * 解析VM模板,生成分页HTML代码.
     * 
     * <h3>maybe you want to return {@link PagerAndContent}</h3>
     * 
     * <blockquote>
     * <p>
     * 你可以拿到结果,再次封装成 {@link PagerAndContent}
     * </p>
     * </blockquote>
     * 
     * @param pagerParams
     *            构造分页需要的请求参数
     * @return 如果 <code>pagerParams</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 {@link PagerParams#getTotalCount()}{@code <=0} 返回 {@link StringUtils#EMPTY} <br>
     *         否则 生成分页html代码
     */
    public static String buildContent(PagerParams pagerParams){
        Validate.notNull(pagerParams, "pagerParams can't be null!");

        if (pagerParams.getTotalCount() <= 0){
            log.debug("totalCount value is [{}] not > 0,will return empty", pagerParams.getTotalCount());
            return EMPTY;// 如果总数不>0 则直接返回 empty,页面分页地方显示空白
        }

        //---------------------------------------------------------------
        if (log.isTraceEnabled()){
            log.trace("input [pagerParams] info:{}", JsonUtil.toString(pagerParams));
        }

        return SimpleTagParamCacheManager.getContent(pagerParams, PagerCacheContentBuilder.INSTANCE);
    }
}

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
package com.feilong.taglib.display.httpconcat;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.util.MapUtil.newConcurrentHashMap;
import static com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;

import java.util.List;
import java.util.Map;

import com.feilong.core.Validate;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.taglib.display.httpconcat.builder.ResultBuilder;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.taglib.display.httpconcat.handler.ItemSrcListResolver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * http concat的核心工具类.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>你可以访问 wiki 查看更多 <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat">feilongDisplay-concat</a></li>
 * <li>类加载的时候,会使用 {@link ResourceBundleUtil} 来读取<code> config/httpconcat </code> 配置文件中的 css模板 以及 JS模板<br>
 * </li>
 * <li>请确保文件路径中有配置文件,以及正确的key,如果获取不到,会 throw {@link IllegalArgumentException}</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see HttpConcatTag
 * @see HttpConcatParam
 * @see "org.apache.commons.collections4.map.LRUMap"
 * @see <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat">feilongDisplay-concat</a>
 * @since 1.0.7
 */
//XXX 丰富 JavaDOC
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpConcatUtil{

    /**
     * 将结果缓存到map.
     * 
     * <p>
     * key是入参 {@link HttpConcatParam}对象,value是解析完的字符串<br>
     * 该cache里面value不会存放null/empty
     * </p>
     * 
     * @since 1.0.7
     */
    private static final Map<HttpConcatParam, String> CACHE = newConcurrentHashMap(500);

    //---------------------------------------------------------------

    /**
     * 获得解析的内容.
     *
     * @param httpConcatParam
     *            the http concat param
     * @return
     *         <ul>
     *         <li>如果 <code>httpConcatParam</code> 是null,抛出 {@link NullPointerException}</li>
     *         <li>如果 isNullOrEmpty httpConcatParam.getItemSrcList() ,返回 {@link StringUtils#EMPTY}</li>
     *         <li>如果支持 concat,那么生成concat字符串</li>
     *         <li>如果不支持 concat,那么生成多行js/css 原生的字符串</li>
     *         </ul>
     */
    public static String getWriteContent(HttpConcatParam httpConcatParam){
        Validate.notNull(httpConcatParam, "httpConcatParam can't be null!");
        if (log.isTraceEnabled()){
            log.trace("input httpConcatParam info:[{}]", JsonUtil.toString(httpConcatParam));
        }

        //---------------------------------------------------------------
        //是否使用cache
        boolean cacheEnable = GLOBAL_CONFIG.getDefaultCacheEnable();
        if (cacheEnable){
            int cacheSize = CACHE.size();
            int cacheKeyHashCode = httpConcatParam.hashCode();

            String content = CACHE.get(httpConcatParam);
            if (null == content){
                log.debug("concatCache size:[{}] no contains current concatParam hashcode:[{}],will parse", cacheSize, cacheKeyHashCode);
            }else{
                log.debug("hashcode:[{}],get httpConcat info from httpConcatCache,cache.size:[{}]", cacheKeyHashCode, cacheSize);
                return content;
            }
        }

        //---------------------------------------------------------------
        List<String> itemSrcList = ItemSrcListResolver.resolve(httpConcatParam.getContent(), httpConcatParam.getDomain());
        if (isNullOrEmpty(itemSrcList)){
            if (log.isWarnEnabled()){
                log.warn(
                                "need itemSrcList to create links but isNullOrEmpty,return [empty],httpConcatParam info:[{}]",
                                JsonUtil.toString(httpConcatParam));
            }
            return EMPTY;
        }

        //---------------------------------------------------------------
        String content = ResultBuilder.build(itemSrcList, httpConcatParam);

        //---------------------------------------------------------------
        after(content, isWriteCache(cacheEnable, httpConcatParam), httpConcatParam);

        return content;
    }

    //---------------------------------------------------------------

    /**
     * Checks if is write cache.
     *
     * @param cacheEnable
     *            the cache enable
     * @param httpConcatParam
     *            the http concat param
     * @return true, if is write cache
     * @since 1.11.1
     */
    private static boolean isWriteCache(boolean cacheEnable,HttpConcatParam httpConcatParam){
        if (!cacheEnable){
            return false;
        }

        //---------------------------------------------------------------
        int cacheSize = CACHE.size();

        //---------------------------------------------------------------

        //超出cache 数量
        int defaultSizeLimit = GLOBAL_CONFIG.getDefaultCacheSizeLimit();
        boolean outOfCacheItemSizeLimit = cacheSize >= defaultSizeLimit;
        if (!outOfCacheItemSizeLimit){
            return true;
        }
        //---------------------------------------------------------------
        int hashCode = httpConcatParam.hashCode();
        log.warn("hashcode:[{}],cache.size:[{}] >= defaultSizeLimit:[{}],will not put to cache", hashCode, cacheSize, defaultSizeLimit);
        //超过,那么就不记录cache
        return false;
    }

    //---------------------------------------------------------------

    /**
     * After.
     *
     * @param content
     *            the content
     * @param isWriteCache
     *            the is write cache
     * @param httpConcatParam
     *            the http concat param
     * @since 1.11.1
     */
    private static void after(String content,boolean isWriteCache,HttpConcatParam httpConcatParam){
        if (log.isDebugEnabled()){
            log.debug("return content:[{}],length:[{}]", content, content.length());
        }

        //--------------------------设置cache-------------------------------------
        int hashCode = httpConcatParam.hashCode();
        if (isWriteCache){
            CACHE.put(httpConcatParam, content);
            log.debug("hashcode:[{}] put to cache,cache size:[{}]", hashCode, CACHE.size());
            return;
        }

        //---------------------------------------------------------------
        //不可以写cache
        if (GLOBAL_CONFIG.getDefaultCacheEnable()){
            log.warn("hashcode:[{}],DEFAULT_CACHEENABLE:[true],but writeCache:[false],so httpConcat result not put to cache", hashCode);
        }
    }

}
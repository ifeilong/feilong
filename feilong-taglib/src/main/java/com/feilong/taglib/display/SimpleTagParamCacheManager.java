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
package com.feilong.taglib.display;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.taglib.SimpleTagStringCacheManager;

/**
 * 标签缓存管理器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "com.google.common.cache.Cache"
 * @see org.apache.commons.collections4.map.LRUMap
 * @since 1.5.4
 * @deprecated pls use {@link SimpleTagStringCacheManager}
 */
//XXX 将来可能会有更好的做法
@Deprecated
public final class SimpleTagParamCacheManager{

    /** The Constant LOGGER. */
    private static final Logger                  LOGGER       = LoggerFactory.getLogger(SimpleTagParamCacheManager.class);

    /**
     * 设置缓存是否开启.
     */
    private static final boolean                 CACHE_ENABLE = true;

    /**
     * 将结果缓存到map.
     * <p>
     * key是入参对象,value是解析完的字符串<br>
     * 该cache里面value不会存放null/empty
     * </p>
     */
    private static final Map<CacheParam, Object> CACHE        = new ConcurrentHashMap<>();

    //-----------------------------------------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SimpleTagParamCacheManager(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //-------------------------------------------------------------------------------------------

    /**
     * Builds the options.
     *
     * @param <T>
     *            the generic type
     * @param <V>
     * @param cacheParam
     *            the option param
     * @param cacheContentBuilder
     *            the cache content builder
     * @return the string
     * @since 1.10.3
     */
    public static <T extends CacheParam, V> V getContent(T cacheParam,CacheContentBuilder<T, V> cacheContentBuilder){
        V contentValue = getContentFromCache(cacheParam);
        if (isNotNullOrEmpty(contentValue)){
            return contentValue;
        }

        //-------------------------------------------------------------------------------------------
        contentValue = cacheContentBuilder.build(cacheParam);

        put(cacheParam, contentValue);

        //-------------------------------------------------------------------------------------------
        return contentValue;
    }

    //---------------------------------------------------------------

    /**
     * 从缓存中读取.
     * 
     * @param <V>
     *
     * @param cacheParam
     *            the pager params
     * @return the content from cache
     */
    private static <V> V getContentFromCache(CacheParam cacheParam){
        if (!CACHE_ENABLE){
            LOGGER.info("the cache status is disable!");
            return null;
        }

        //-----------------------------------------------------------------------------

        int hashCode = cacheParam.hashCode();
        int size = CACHE.size();
        String name = cacheParam.getClass().getSimpleName();

        if (CACHE.containsKey(cacheParam)){
            LOGGER.debug("cacheSize:[{}],[{}](hashcode:[{}]),hit cache,get from cache", size, name, hashCode);
            return (V) CACHE.get(cacheParam);
        }

        LOGGER.debug("cacheSize:[{}],cache not contains [{}](hashcode:[{}]),will do parse", size, name, hashCode);
        return null;
    }

    //---------------------------------------------------------------

    /**
     * 设置.
     * 
     * @param <V>
     *
     * @param cacheParam
     *            the pager params
     * @param content
     *            the content
     */
    private static <V> void put(CacheParam cacheParam,V content){
        if (CACHE_ENABLE && isNotNullOrEmpty(content)){//设置cache
            CACHE.put(cacheParam, content);
        }
    }
}

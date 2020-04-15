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
package com.feilong.taglib;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 标签缓存管理器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "com.google.common.cache.Cache"
 * @see org.apache.commons.collections4.map.LRUMap
 * @since 1.10.5
 */
//XXX 将来可能会有更好的做法
public final class SimpleTagStringCacheManager{

    /** The Constant LOGGER. */
    private static final Logger              LOGGER       = LoggerFactory.getLogger(SimpleTagStringCacheManager.class);

    /**
     * 设置缓存是否开启.
     */
    private static final boolean             CACHE_ENABLE = true;

    /**
     * 将结果缓存到map.
     * <p>
     * key是相关字符串,value是解析完的字符串<br>
     * 该cache里面value不会存放null/empty
     * </p>
     */
    private static final Map<String, Object> CACHE        = new ConcurrentHashMap<>();

    //-----------------------------------------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SimpleTagStringCacheManager(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //-------------------------------------------------------------------------------------------

    /**
     * Builds the options.
     *
     * @param key
     *            the key
     * @return the string
     */
    public static Object get(String key){
        if (!CACHE_ENABLE){
            LOGGER.info("when to get key:[{}] data from cache, but cache's status is disable!", key);
            return null;
        }

        //-----------------------------------------------------------------------------
        int size = CACHE.size();

        if (CACHE.containsKey(key)){
            LOGGER.debug("cacheSize:[{}],key:[{}],hit cache,get from cache", size, key);
            return CACHE.get(key);
        }

        LOGGER.debug("cacheSize:[{}],not contains [{}],will do parse", size, key);
        return null;
    }

    //---------------------------------------------------------------

    /**
     * 设置.
     *
     * @param cacheParam
     *            the pager params
     * @param content
     *            the content
     */
    public static void put(String cacheParam,Object content){
        if (CACHE_ENABLE && isNotNullOrEmpty(content)){//设置cache
            CACHE.put(cacheParam, content);
        }
    }
}

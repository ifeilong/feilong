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
package com.feilong.core.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * SPI工具类.
 * 
 * <p>
 * SPI（Service Provider Interface）机制是Java中用于动态加载服务实现的核心技术，其底层实现基于配置文件、类加载器与反射机制。
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.4.0
 */
@Slf4j
public class ServiceLoaderUtil{

    /** The Constant CACHE. */
    private static final Map<Class<?>, Optional<?>> CACHE = new ConcurrentHashMap<>();

    //---------------------------------------------------------------
    /**
     * 获取接口的首个 SPI 实现.
     *
     * @param <T>
     *            the generic type
     * @param spiClass
     *            the spi class
     * @return the t
     */
    public static <T> T loadFirst(Class<T> spiClass){
        Optional<?> result = CACHE.computeIfAbsent(spiClass, clazz -> {
            ServiceLoader<T> loader = ServiceLoader.load(spiClass);//不需要判断loader是否为null，因为ServiceLoader.load()永远不会返回null；
            Iterator<T> iterator = loader.iterator();
            try{
                if (iterator.hasNext()){
                    return Optional.ofNullable(iterator.next());
                }
            }catch (ServiceConfigurationError | Exception | NoClassDefFoundError e){
                log.error("SPI实现类加载失败: {}", spiClass.getName(), e);
            }
            return Optional.empty(); // 统一返回 Optional 类型
        });

        // 安全转换
        return result.map(obj -> spiClass.cast(obj)).orElse(null);
    }

}

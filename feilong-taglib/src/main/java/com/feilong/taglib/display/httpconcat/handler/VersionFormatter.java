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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;

import com.feilong.lib.lang3.StringUtils;
import com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * version 值格式化.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VersionFormatter{

    /**
     * Format.
     *
     * @param version
     *            the version
     * @return 如果 version 是null或者是 empty,那么直接返回 {@link StringUtils#EMPTY}<br>
     *         如果 version 有值,判断 {@link HttpConcatGlobalConfigBuilder#GLOBAL_CONFIG} versionEncode 配置, 如果没有配置, 那么直接返回 version参数<br>
     *         如果有值, 那么将调用 {@link VersionEncodeUtil#encode(String, String)}<br>
     * @see com.feilong.taglib.display.httpconcat.handler.VersionEncodeUtil#encode(String, String)
     */
    public static String format(String version){
        if (isNullOrEmpty(version)){
            return EMPTY;
        }

        //---------------------------------------------------------------
        String versionEncode = GLOBAL_CONFIG.getVersionEncode();
        if (isNullOrEmpty(versionEncode)){
            return version;
        }

        return VersionEncodeUtil.encode(version, versionEncode);
    }

}

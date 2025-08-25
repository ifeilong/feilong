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

import static com.feilong.core.bean.ConvertUtil.toLocale;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feilong.taglib.display.loadbundle.LoadBundleParam;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class LocaleSupportUtil.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocaleSupportUtil{

    /**
     * 将 locale 转成 {@link Locale}.
     *
     * @param locale
     *            the locale
     * @param request
     *            the request
     * @return 如果 locale 是null,那么将会使用 {@link javax.servlet.ServletRequest#getLocale()}
     */
    public static Locale toLocal(Object locale,HttpServletRequest request){
        return defaultIfNull(toLocale(locale), request.getLocale());
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param <T>
     *            the generic type
     * @param bundleParam
     *            the bundle param
     * @return the map
     */
    public static <T extends LoadBundleParam> Map<String, String> build(T bundleParam){
        //获得 key value map.
        return toMap(getResourceBundle(bundleParam.getBaseName(), bundleParam.getLocale()));
    }
}

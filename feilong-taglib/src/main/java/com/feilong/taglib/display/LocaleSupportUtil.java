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
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feilong.taglib.display.loadbundle.LoadBundleParam;

/**
 * The Class LocaleSupportUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public final class LocaleSupportUtil{

    /** Don't let anyone instantiate this class. */
    private LocaleSupportUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

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

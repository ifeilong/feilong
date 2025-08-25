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
package com.feilong.taglib.display.sensitive;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toIntegers;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;

import java.util.Map;

import com.feilong.core.lang.StringUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class Sensitive1.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.14.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SensitivePropertiesConfig{

    /** <code>{@value}</code>. */
    private static final Map<String, String> SENSITIVE_CONFIG_MAP = toMap(getResourceBundle("config/sensitive"));

    /** The Constant PREFIX. */
    private static final String              PREFIX               = "sensitive.";

    //---------------------------------------------------------------

    /**
     * Load.
     *
     * @param type
     *            the type
     * @return the integer[]
     */
    @SuppressWarnings("squid:S1168") //Empty arrays and collections should be returned instead of null
    public static Integer[] load(String type){
        //配置式
        String leftAndRightNoMaskLengthsString = SENSITIVE_CONFIG_MAP.get(PREFIX + type.toLowerCase());
        if (isNullOrEmpty(leftAndRightNoMaskLengthsString)){
            return null;
        }
        return toIntegers(StringUtil.tokenizeToStringArray(leftAndRightNoMaskLengthsString, ","));
    }
}

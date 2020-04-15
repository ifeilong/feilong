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

/**
 * The Class Sensitive1.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.14.0
 */
public final class SensitivePropertiesConfig{

    /** <code>{@value}</code>. */
    private static final Map<String, String> SENSITIVE_CONFIG_MAP = toMap(getResourceBundle("config/sensitive"));

    /** The Constant PREFIX. */
    private static final String              PREFIX               = "sensitive.";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SensitivePropertiesConfig(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Load.
     *
     * @param type
     *            the type
     * @return the integer[]
     */
    public static Integer[] load(String type){
        //配置式
        String leftAndRightNoMaskLengthsString = SENSITIVE_CONFIG_MAP.get(PREFIX + type.toLowerCase());
        if (isNullOrEmpty(leftAndRightNoMaskLengthsString)){
            return null;
        }
        return toIntegers(StringUtil.tokenizeToStringArray(leftAndRightNoMaskLengthsString, ","));
    }
}

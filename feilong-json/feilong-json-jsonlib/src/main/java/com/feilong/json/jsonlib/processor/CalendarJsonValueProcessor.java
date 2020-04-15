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
package com.feilong.json.jsonlib.processor;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;

import java.util.Calendar;

import com.feilong.core.date.DateUtil;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * {@link Calendar} 转换日期值处理器实现.
 * 
 * <p>
 * 为了简化操作,{@link com.feilong.json.jsonlib.builder.JsonConfigBuilder#buildDefaultJavaToJsonConfig()} 内置了
 * <code>new DateJsonValueProcessor(COMMON_DATE_AND_TIME)</code>
 * ,如果你想输出成其他的日期格式,也可以使用这个类来提前渲染
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.14.0
 */
public class CalendarJsonValueProcessor extends AbstractJsonValueProcessor{

    /** 默认Singleton instance. */
    public static final JsonValueProcessor DEFAULT_INSTANCE = new CalendarJsonValueProcessor(COMMON_DATE_AND_TIME);

    /** The date pattern. */
    private String                         datePattern      = COMMON_DATE_AND_TIME;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param datePattern
     *            你可以使用 {@link com.feilong.core.DatePattern}
     */
    public CalendarJsonValueProcessor(String datePattern){
        this.datePattern = datePattern;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.jsonlib.processor.AbstractJsonValueProcessor#processValue(java.lang.Object, net.sf.json.JsonConfig)
     */
    @Override
    protected Object processValue(Object value,JsonConfig jsonConfig){
        if (null == value){
            return null;
        }
        return value instanceof Calendar ? //
                        DateUtil.toString(((Calendar) value).getTime(), datePattern) : value.toString();
    }
}
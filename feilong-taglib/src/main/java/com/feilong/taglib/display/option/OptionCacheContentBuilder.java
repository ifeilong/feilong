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
package com.feilong.taglib.display.option;

import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Map;

import com.feilong.taglib.display.CacheContentBuilder;
import com.feilong.taglib.display.LocaleSupportUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 用来构造输出 option内容.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class OptionCacheContentBuilder implements CacheContentBuilder<OptionParam, String>{

    /** Static instance. */
    // the static instance works for all types
    public static final CacheContentBuilder<OptionParam, String> INSTANCE        = new OptionCacheContentBuilder();

    //---------------------------------------------------------------
    /** option 格式 <code>{@value}</code>. */
    private static final String                                  OPTION_PATTERN  = "<option value=\"{}\"{}>{}</option>";

    /** 选中的字符串 <code>{@value}</code>. */
    private static final String                                  SELECTED_STRING = " selected=\"selected\"";

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.display.CacheContentBuilder#build(com.feilong.taglib.display.CacheParam)
     */
    @Override
    public String build(OptionParam optionParam){
        StringBuilder sb = new StringBuilder();

        //获得 key value map.
        Map<String, String> map = LocaleSupportUtil.build(optionParam);

        for (Map.Entry<String, String> entry : map.entrySet()){
            String option = buildOption(entry.getKey(), entry.getValue(), optionParam.getSelectedKey());
            sb.append(option).append(lineSeparator());
        }
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Builds the option.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param selectedKey
     *            the selected key
     * @return the string
     * @since 1.8.1
     */
    private static String buildOption(String key,String value,String selectedKey){
        String selectedStatus = key.equals(selectedKey) ? SELECTED_STRING : EMPTY;

        //主要为了国际化使用, 页面显示的时候,显示为  "<option value="edu.option1">初中</option>"
        //存储到数据库的时候 值存储的是  edu.option1
        //而 显示的时候,使用 spring message 标签显示
        return Slf4jUtil.format(OPTION_PATTERN, key, selectedStatus, value);
    }

}

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
package com.feilong.context.format;

import com.feilong.json.JsonUtil;

/**
 * json格式的格式化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.2
 */
public class JsonStringFormatter extends AbstractStringFormatter{

    /** Static instance. */
    // the static instance works for all types
    public static final StringFormatter INSTANCE = new JsonStringFormatter();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.format.AbstractStringFormatter#doFormat(java.lang.String)
     */
    @Override
    protected String doFormat(String str){
        return JsonUtil.format(str);
    }
}

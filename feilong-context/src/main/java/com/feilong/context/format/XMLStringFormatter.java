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

import com.feilong.xml.XmlUtil;

/**
 * 将字符串转成xml格式化输出.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.2
 */
public class XMLStringFormatter extends AbstractStringFormatter{

    /** Static instance. */
    // the static instance works for all types
    public static final StringFormatter INSTANCE = new XMLStringFormatter();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.format.AbstractStringFormatter#doFormat(java.lang.String)
     */
    @Override
    protected String doFormat(String str){
        return XmlUtil.format(str);
    }
}

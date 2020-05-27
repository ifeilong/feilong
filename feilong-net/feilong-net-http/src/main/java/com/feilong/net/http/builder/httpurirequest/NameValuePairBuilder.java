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
package com.feilong.net.http.builder.httpurirequest;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 将Map{@code <String, String>} 转成 List{@code <NameValuePair>} 的构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
final class NameValuePairBuilder{

    /** Don't let anyone instantiate this class. */
    private NameValuePairBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将Map{@code <String, String>} 转成 List{@code <NameValuePair>}.
     *
     * @param paramMap
     *            the param map
     * @return 如果 <code>paramMap</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     * @see org.apache.http.NameValuePair
     * @see org.apache.http.message.BasicNameValuePair
     */
    static List<NameValuePair> build(Map<String, String> paramMap){
        if (isNullOrEmpty(paramMap)){
            return emptyList();
        }

        //---------------------------------------------------------------
        List<NameValuePair> params = newArrayList();
        for (Map.Entry<String, String> entry : paramMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            params.add(new BasicNameValuePair(key, value));
        }
        return params;
    }
}

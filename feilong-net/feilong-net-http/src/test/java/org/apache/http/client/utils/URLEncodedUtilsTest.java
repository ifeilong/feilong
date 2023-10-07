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
package org.apache.http.client.utils;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import org.junit.Test;

import com.feilong.lib.org.apache.http.NameValuePair;
import com.feilong.lib.org.apache.http.client.utils.URLEncodedUtils;
import com.feilong.lib.org.apache.http.message.BasicNameValuePair;
import com.feilong.test.AbstractTest;

/**
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.6
 */
public class URLEncodedUtilsTest extends AbstractTest{

    @Test
    public final void test(){
        List<NameValuePair> list = newArrayList();
        list.add(new BasicNameValuePair("name", "jinxin"));
        list.add(new BasicNameValuePair("name1", "jinxin1"));
        list.add(new BasicNameValuePair("name2", "jinxin2"));
        list.add(new BasicNameValuePair("name", "jinxin"));
        LOGGER.debug(URLEncodedUtils.format(list, UTF8));
    }

    @Test
    public final void test1(){
        List<NameValuePair> list = newArrayList();
        list.add(new BasicNameValuePair("name", "%"));
        LOGGER.debug(URLEncodedUtils.format(list, UTF8));
    }
}

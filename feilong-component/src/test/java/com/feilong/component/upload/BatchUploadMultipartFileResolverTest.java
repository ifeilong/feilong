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
package com.feilong.component.upload;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;
import static com.feilong.core.lang.SystemUtil.USER_HOME;

import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class BatchUploadMultipartFileResolverTest extends AbstractUploadMultipartFileResolverTest{

    @Test(expected = NullPointerException.class)
    public void testDefaultMultipartFileResolverTestNull(){
        multipartFileResolver.upload(null, USER_HOME, toArray("1.jpg"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestEmpty(){
        MultipartFile[] multipartFile = new MultipartFile[0];
        multipartFileResolver.upload(multipartFile, USER_HOME, toArray("1.jpg", "2.jpg"));
    }

    //---------------------------------------------------------------
    @Test(expected = NullPointerException.class)
    public void testDefaultMultipartFileResolverTestNull22(){
        multipartFileResolver.upload(toArray(build()), null, toArray("1.jpg"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestEmpty22(){
        multipartFileResolver.upload(toArray(build()), "", toArray("1.jpg"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestBlank22(){
        multipartFileResolver.upload(toArray(build()), " ", toArray("1.jpg"));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testDefaultMultipartFileResolverTestNull11(){
        multipartFileResolver.upload(toArray(build()), USER_HOME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestEmpty11(){
        multipartFileResolver.upload(toArray(build()), USER_HOME, EMPTY_STRING_ARRAY);
    }

}
